require 'json'

include_class Java::Javattt.Side
include_class Java::Javattt.Game
include_class Java::Javattt.TransitionData
include_class Java::Javattt.TransitionData.Signal

class Java::Javattt.Side
	field_reader :X, :O
end

class HTTPGame < Java::Javattt.Game
	@@games = {}

	def initialize ui
		super.initialize(ui)
		touch
	end

	def touch
		@timestamp = Time.now.to_i
	end

	def status
		stat = {}
		stat["timestamp"] = @timestamp
		stat["board"] = @board.map do |row|
			row.map do |sym|
				if sym == Side.X
					"X"
				elsif sym == Side.O
					"O"
				else
					" "
				end
			end.to_a
		end.to_a

		JSON.generate stat
	end


	def self.interpret params
		if params[:signal] == true
			TransitionData.new Signal.YES
		elsif params[:signal] == false
			TransitionData.new Signal.NO
		elsif params[:signal] == "EXIT"
			TransitionData.new Signal.EXIT
		elsif params[:signal] == "MOVE"
			move = [params[:x].to_i, params[:y].to_i].to_java :int
			TransitionData.new move
		end
	end

	def self.[](conn)
		@@games[conn]
	end
end