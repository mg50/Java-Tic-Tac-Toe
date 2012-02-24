require 'json'
require 'pry'

require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
include_class Java::Javattt.Side
include_class Java::Javattt.Game
include_class Java::Javattt.Stage
include_class Java::Javattt.TransitionData
include_class Java::Javattt.TransitionData::Signal

Java::Javattt.Side.class_eval do
	field_reader :X, :O
end

class HTTPGame < Java::Javattt.Game
	@@games = {}

	def initialize 
		touch
	end

	def touch
		@timestamp = Time.now.to_i
	end

	def started
		stage == Stage.newGame
	end

	def status
		stat = {}
		stat["timestamp"] = @timestamp
		stat["stage"] = stage.toString
		stat["board"] = board.grid.map do |row|
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

		JSON.generate stat
	end


	def interpret params

		if params["signal"] == true
			ret = TransitionData.new Signal::YES
		elsif params["signal"] == false
			ret = TransitionData.new Signal::NO
		elsif params["signal"] == "EXIT"
			ret = TransitionData.new Signal::EXIT
		elsif params["signal"] == "MOVE"
			x, y = params["coords"]
			move = [x, y].to_java :int
			ret = TransitionData.new move
		end

		ret
	end

	def self.[](conn)
		@@games[conn]
	end
end