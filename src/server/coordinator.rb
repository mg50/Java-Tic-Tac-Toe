require 'rubygems'
require 'java'
require File.expand_path('../../javattt/ttt.jar', __FILE__)
include_class Java::Javattt.Game
include_class Java::Javattt.WebUI


class Coordinator
	@@games = {}

	def self.games
		@@games
	end

	def self.clear_all_games
		@@games = {}
	end

	def self.new_game id1, id2
		if not @@games[id1] and not @@games[id2]
			ui = Java::Javattt.WebUI.new
			game = Java::Javattt.Game.new ui
			@@games[id1] = @@games[id2] = game.touch
		elsif @@games[id1] && @@games[id1] == @@games[id2]
			@@games[id1]
		else
			nil
		end
	end

	def self.find_game id
		game = @@games[id]
		if Time.now - game.timestamp > 3000
	end

	def self.end_game id
		the_game = @@games[id]
		return if the_game.nil?

		@@games.each do |id, game|
			@@games.delete(id) if game == the_game
		end
	end

end