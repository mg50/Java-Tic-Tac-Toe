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
	@@games = {}

	attr_accessor :client_player, :waiting_for_second_player

	def self.[](room_name)
		@@games[room_name] ||= HTTPGame.new
	end

	def initialize
		super
	end

	def add_player(player)
		if not self.masterPlayer
			self.masterPlayer = player
		elsif not client_player
			client_player = player unless client_player

			if waiting_for_second_player
				waiting_for_second_player = false
				self.start
			end
		end
	end

	def remove_player(player)
		if player == self.masterPlayer
			self.masterPlayer = client_player
			client_player = nil
		elsif player == client_player
			client_player = nil
		end

		@@games
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
		when "ACKNOWLEDGED"
			player.ui.alert = nil
		when "MOVE"
			coords = opts["coords"].to_java :int
			cmd = MoveCommand.new coords if player == self.currentPlayer and self.state.is_a? MoveState and
				  not waiting_for_second_player
		end

		self.start cmd if cmd
	end

	def onStateTransition
		if client_player
			if not self.state.is_a? MoveState
				client_player.ui.wait_message = "Waiting for player one to begin two-player game."
			else
				client_player.ui.wait_message = nil
			end
		end
	end

	def two_player?
		self.playerX.gameStrategy.is_a? HumanStrategy and self.playerO.gameStrategy.s_a? HumanStrategy
	end

	def onNewGame
		self.masterPlayer.ui.alert_message = nil
		self.masterPlayer.ui.wait_message = nil
		waiting_for_second_player = false
	end

	def onBeginningGame
		return unless two_player?

		if not client_player
			self.masterPlayer.wait_message = "Waiting for second player to join."
			waiting_for_second_player = true
		else
			self.masterPlayer.wait_message = nil
			self.client_player.wait_message = nil if self.client_player

			if self.masterPlayer = self.playerX
				self.playerO = client_player
				client_player.side = Side.O
			else
				self.playerX = client_player
				client_player.side = Side.X
			end
		end
	end
end