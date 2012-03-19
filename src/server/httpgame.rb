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
include_class Java::Javattt.command.RestartCommand
include_class Java::Javattt.fsm.NewGameState
include_class Java::Javattt.fsm.MoveState

Java::Javattt.Side.class_eval do
	field_reader :X, :O
end

class HTTPGame < Java::Javattt.Game
	@@games = {}

	attr_accessor :client_player, :waiting_for_second_player

	def self.clear_all_games
		@@games = {}
	end

	def self.random_empty_room_name
		room = nil
		while room.nil? or @@games[room]
			room = rand(1000000).to_s
		end

		room
	end

	def self.[](lookup)
		if lookup.is_a? String
			@@games[lookup] ||= HTTPGame.new
			return @@games[lookup]
		elsif lookup.is_a? HTTPPlayer
			@@games.each do |name, game|
				return game if game.masterPlayer == lookup or game.client_player == lookup
			end
		end

		nil
	end

	def initialize
		super
	end

	def add_player(player)
		if old_game = HTTPGame[player] and old_game != self
			old_game.remove_player player
		elsif old_game == self
			return
		end

		if not self.masterPlayer
			self.masterPlayer = player
			self.start
		elsif not client_player
			self.client_player = player

			self.start
		end
	end

	def remove_player(player)
		if player == self.masterPlayer

			self.masterPlayer = self.client_player
			self.client_player = nil

			if self.masterPlayer
				self.receive_signal self.masterPlayer, "RESTART"
				self.masterPlayer.alert_message "The game owner has left the game."				
			end

		elsif player == self.client_player
			if two_player? and self.state.is_a? MoveState
				self.receive_signal self.masterPlayer, "RESTART"
				self.masterPlayer.alert_message "Your opponent has left the game."				
			end

			self.client_player = nil
		end

		@@games
	end

	def valid_player?(player)
		player == self.masterPlayer or player == client_player
	end

	def get_ruby_grid
		self.board.grid.map do |row|
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

	def status(player)
		return unless valid_player? player 

		{:board => get_ruby_grid,
		 :state => self.state.class.name.split("::").last,
		 :currentPlayer => (if player == self.currentPlayer
		 						"you"
		 					elsif self.currentPlayer
		 						self.currentPlayer == self.playerX ? "X" : "O"
		 					else
		 						nil
		 					end),
 		 :ui => player.ui_status}
	end

	def receive_signal(player, signal, opts={})
		cmd = nil
		
		case signal
		when "YES"
			cmd = YesCommand.new if player == self.masterPlayer
		when "NO"
			cmd = NoCommand.new if player == self.masterPlayer
		when "INVALID"
			cmd = InvalidCommand.new if player == self.masterPlayer
		when "RESTART"
			cmd = RestartCommand.new if player == self.masterPlayer
		when "EXIT"
			remove_player player
		when "ALERT_OK"
			player.alert_message nil
		when "MOVE"
			coords = opts["coords"].to_java :int
			cmd = MoveCommand.new coords if player == self.currentPlayer and self.state.is_a? MoveState and
				  self.ready_for_game_start
		end

		self.start cmd if cmd
	end

	def onStateTransition
		if client_player
			if not two_player? or not self.state.is_a? MoveState
				client_player.wait_message "Waiting for player one to begin two-player game."
			else
				client_player.wait_message nil
			end
		end
	end

	def two_player?
		self.playerX.gameStrategy.is_a? HumanStrategy and self.playerO.gameStrategy.is_a? HumanStrategy
	end

	def onNewGame
		self.masterPlayer.alert_message nil if self.masterPlayer
		self.masterPlayer.wait_message  nil if self.masterPlayer
		waiting_for_second_player = false
	end

	def onRestart
		if client_player
			client_player.alert_message "Your opponent has restarted the game."
		end
	end

	def readyForGameStart
		return true unless two_player?

		if not client_player
			self.masterPlayer.wait_message "Waiting for second player to join. " + 
				"(Send the URL to another person to invite him to the game)."
			return false
		else
			self.masterPlayer.wait_message nil
			self.client_player.wait_message nil if self.client_player

			if self.masterPlayer == self.playerX
				self.playerO = client_player
				client_player.side = Side.O
			else
				self.playerX = client_player
				client_player.side = Side.X
			end

			return true
		end
	end
end