require 'json'
require 'pry'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'HTTPUI')
include_class Java::Javattt.Board
include_class Java::Javattt.Side
include_class Java::Javattt.Game
include_class Java::Javattt.command.YesCommand
include_class Java::Javattt.command.NoCommand
include_class Java::Javattt.command.ExitCommand
include_class Java::Javattt.command.MoveCommand
include_class Java::Javattt.command.AlertCommand
include_class Java::Javattt.command.RestartCommand
include_class Java::Javattt.command.WaitCommand

Java::Javattt.Side.class_eval do
	field_reader :X, :O
end

class HTTPGame < Java::Javattt.Game
	attr_accessor :opponent_game, :waiting_for_opponent, :room, :ip, :timestamp

	@@waiting_games = {}
	@@games = {}

	def self.clear_waiting_games
		@@waiting_games = {}
	end

	def self.[](session)
		unless @@games[session[:session_id]]
			@@games[session[:session_id]] = self.new
			@@games[session[:session_id]].start
		end
		@@games[session[:session_id]]
	end


	def alert(msg)
		AlertCommand.new(msg).sendToGame(self)
		start
	end

	def duplicate_game_state game
		self.board = Board.new(game.board.getGrid)
		self.playerX = game.playerX.class.new(Side.X) if game.playerX
		self.playerO = game.playerO.class.new(Side.O) if game.playerO
		self.currentPlayer = game.currentPlayer == game.playerX ? self.playerX : self.playerO
		self.state = game.state.class.new self
	end

	def initialize
		super
		touch
		self.ui = HTTPUI.new
	end

	def touch
		@timestamp = Time.now.to_f
	end

	def get_ruby_grid
		board.grid.map do |row|
			row.map do |sym|
				if sym == Side.X
					"X"
				elsif sym == Side.O
					"O"
				else
					""
				end
			end.to_a
		end.to_a		
	end

	def status
		stat = {}
		stat["timestamp"] = @timestamp
		stat["state"] = state.class.name.split("::").last
		stat["board"] = get_ruby_grid if board
		stat["currentPlayer"] = currentPlayer == playerX ? "X" : "O"
		stat["message"] = self.ui.message
		stat
	end

	def two_player?
		playerX and playerO and playerX.class == Java::Javattt.HumanPlayer and 
	  		playerO.class == Java::Javattt.HumanPlayer
	end

	def receive_signal params
		if params["signal"] == "YES"
			cmd = YesCommand.new
		elsif params["signal"] == "NO"
			cmd = NoCommand.new
		elsif params["signal"] == "EXIT"
			cmd = ExitCommand.new
		elsif params["signal"] == "RESTART"
			cmd = RestartCommand.new
		elsif params["signal"] == "MOVE"
			return if self.waiting_for_opponent
			move = params["coords"].to_java :int
			cmd = MoveCommand.new move
		elsif params["signal"] == "NULL"
			cmd = NullCommand.new
		elsif params["signal"] == "WAIT"
			cmd = WaitCommand.new
			self.ui.message = params["message"] || ""
		end

		start cmd
	end

	def current_player_human?
		currentPlayer.class == Java::Javattt.HumanPlayer
	end

	def switch_player
		self.currentPlayer = self.currentPlayer == playerX ? playerO : playerX
	end

	#Game hooks

	def onStateTransition
		self.room.suspend_games_until_owner_starts if self.room and not (self.room.owner.playing and
																		 self.room.owner.two_player?)
	end

	def onNewGame
		opponent_game = nil
		self.waiting_for_opponent = false
	end

	def onBeginningGame
		return unless two_player? and self.room and self == self.room.owner

		if opp = self.opponent_game
			opp.duplicate_game_state self

			opp.start
		end
	end

	def onSuccessfulMove(coords)
		touch
		if not two_player?
			self.waiting_for_opponent = !current_player_human?
		else
			return if self.waiting_for_opponent

			opponent_game.start MoveCommand.new coords
			self.waiting_for_opponent = true
			opponent_game.waiting_for_opponent = false
		end
	end

	def onReceivingPlayVsAI
		return unless two_player?

		size = self.board.size.to_i
		self.waiting_for_opponent = true

		@@waiting_games[size] ||= []

		opponent_in_same_room = @@waiting_games[size].find {|game| game.room == self.room}

		if opponent_in_same_room
			self.opponent_game = opponent_in_same_room
			opponent_in_same_room.opponent_game = self
			@@waiting_games[size].delete opponent_in_same_room
			opponent_in_same_room.waiting_for_opponent = false
		else
			@@waiting_games[size] << self
		end
	end

	def onRestart
		if opp = self.opponent_game
			self.opponent_game = nil
			opp.opponent_game = nil

			opp.receive_signal "signal" => "RESTART"
			opp.alert "Your opponent has left the game."			
		end
	end

	def onReceivingPlayAsX
		self.waiting_for_opponent = !(playerX.class == Java::Javattt.HumanPlayer)
	end

	def onHalt
		return unless two_player?

		opponent_game.state = Java::Javattt::fsm.GameOverState.new(opponent_game)
		opponent_game.start nil
	end

	def onGameOver(victor)		
		if victor == Side.X
			str = "X"
		elsif victor == Side.O
			str = "O"
		else
			str = nil
		end

		if str.nil?
			alert "The game ended in a draw."
		else
			alert "Player " + str + " has won!"
		end
	end
end