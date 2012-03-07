require 'json'
require 'pry'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'HTTPUI')
include_class Java::Javattt.Side
include_class Java::Javattt.Game
include_class Java::Javattt.TransitionData
include_class Java::Javattt.TransitionData::Signal

Java::Javattt.Side.class_eval do
	field_reader :X, :O
end

class HTTPGame < Java::Javattt.Game
	attr_accessor :opponent_game, :waiting_for_opponent, :room, :ip

	@@waiting_games = {}

	def self.clear_waiting_games
		@@waiting_games = {}
	end

	def initialize
		super
		touch
		self.ui = HTTPUI.new
	end

	def restart
		self.state = Java::Javattt.fsm.NewGameState.new(self)

		start nil
		if opp = self.opponent_game
			self.opponent_game = nil
			opp.opponent_game = nil

			opp.restart
		end
	end

	def touch
		@timestamp = Time.now.to_i
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
		stat
	end

	def two_player?
		playerX.class == Java::Javattt.HumanPlayer and playerO.class == Java::Javattt.HumanPlayer
	end

	def receive_signal params
		if params["signal"] == "YES"
			data = TransitionData.new Signal::YES
		elsif params["signal"] == "NO"
			data = TransitionData.new Signal::NO
		elsif params["signal"] == "EXIT"
			data = TransitionData.new Signal::EXIT
		elsif params["signal"] == "MOVE"
			return if self.waiting_for_opponent
			move = params["coords"].to_java :int
			data = TransitionData.new move
		end

		start data
	end

	def current_player_human?
		currentPlayer.class == Java::Javattt.HumanPlayer
	end

	def switch_player
		self.currentPlayer = self.currentPlayer == playerX ? playerO : playerX
	end

	#Game hooks
	def onNewGame
		opponent_game = nil
		self.waiting_for_opponent = false
	end

	def onSuccessfulMove(coords)
		touch
		if not two_player?
			self.waiting_for_opponent = !current_player_human?
		else
			return if self.waiting_for_opponent

			opponent_game.start TransitionData.new(coords)
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

	def onReceivingPlayAsX
		self.waiting_for_opponent = !(playerX.class == Java::Javattt.HumanPlayer)
	end

	def onHalt
		return unless two_player?

		opponent_game.state = Java::Javattt::fsm.GameOverState.new(opponent_game)
		opponent_game.start nil
	end
end