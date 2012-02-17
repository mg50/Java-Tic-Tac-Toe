require 'rubygems'
require 'sinatra'
require 'json'
require '../../javattt/ttt.jar'
require 'Java'
include_class Java::Javattt.AIPlayer
include_class Java::Javattt.Board

#$CLASSPATH << "../../javattt/"
#import Game


class Solver
	X = Java::Javattt.Board.X
	O = Java::Javattt.Board.O
	Empty = Java::Javattt.Board.Empty


	def solve(game_string, player)
		player = AIPlayer.new java_side(player)
		board = make_board game_string
		move = player.calculateMove(board).to_a

		move.empty? ? nil : move
	end

	def java_side(side)
		if side == "x"
			X
		elsif side == "o"
			O
		else
			Empty
		end
	end

	def make_board(game_string)

		rows = [game_string[0..2], game_string[3..5], game_string[6..8]]
		puts rows.inspect
		java_rows = rows.map do |row|
			java_row = row.chars.map {|side| java_side side}
		end.to_java(Java::int[])

		Board.new java_rows
	end
end

=begin
def App < Sinatra::Base

	get '/' do
		game_string = params[:gameString]

	end




end
=end


