require 'rubygems'
require 'json'
require 'java'
require File.expand_path('../../javattt/ttt.jar', __FILE__)
include_class Java::Javattt.AIPlayer
include_class Java::Javattt.Board
include_class Java::Javattt.Side

class HTTPPlayer < Java::Javattt.Player

	def determineNextMove
		
	end
end