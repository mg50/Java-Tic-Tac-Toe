require 'rubygems'
require 'json'
require 'java'
require File.expand_path('../../javattt/ttt.jar', __FILE__)
include_class Java::Javattt.AIPlayer
include_class Java::Javattt.Board
include_class Java::Javattt.Side
include_class Java::Javattt.Game


class Coordinator
	@@games = {}
	@@last_game_id = 0

	def new_game
		game = Java::Javattt.Game.new
		@@games[@@last_game_id] = game
		@@last_game_id += 1
	end

	def store(id)
		games[id] = Marshal.dump games[id] if games[id]
	end

	def load(id)
		return if not games[id]
		games[id] = Marshal.load games[id]
		games[id]
	end
end

class Game
	include Java::Javattt.Game


end

class NetPlayer < Java::Javattt.Player
	def initialize(side)
		@automated = false
		super(side)
	end

	def calculateMove
		
	end
end






