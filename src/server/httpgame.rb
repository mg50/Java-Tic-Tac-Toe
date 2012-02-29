require 'json'
require 'pry'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'HTTPUI')
include_class Java::Javattt.Side
include_class Java::Javattt.Game
include_class Java::Javattt.Stage
include_class Java::Javattt.TransitionData
include_class Java::Javattt.TransitionData::Signal

Java::Javattt.Side.class_eval do
	field_reader :X, :O
end

class HTTPGame < Java::Javattt.Game
	attr_accessor :opponent, :waiting_for_opponent

	@@waiting_games = {}

	def initialize
		super
		touch
		self.ui = HTTPUI.new
	end

	def touch
		@timestamp = Time.now.to_i
	end

	def started?
		stage == Stage.newGame
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
		stat["stage"] = stage.toString
		stat["board"] = get_ruby_grid
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
		opponent = nil
		self.waiting_for_opponent = false
	end

	def onSuccessfulMove(move)
		touch
		if not two_player?
			self.waiting_for_opponent = !current_player_human?
			#binding.pry
		else
			return if self.waiting_for_opponent

			opponent.start move
			self.waiting_for_opponent = true
			opponent.waiting_for_opponent = false
		end
	end

	def onReceivingPlayVsAI
		return unless two_player?

		size = self.board.size.to_i
		self.waiting_for_opponent = true

		if @@waiting_games[size].nil? or @@waiting_games[size].empty?
			@@waiting_games[size] = [self]
		else
			self.opponent = @@waiting_games[size].shift
			opponent.opponent = self
			opponent.waiting_for_opponent = false
		end
	end

	def onReceivingPlayAsX(data)
		self.waiting_for_opponent = !current_player_human?		
	end

	def onHalt
		return unless two_player?

		opponent.stage = Java::Javattt.Stage::gameOver
		opponent.start nil
	end
end