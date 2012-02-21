require 'rubygems'
require 'json'
require 'java'
require File.expand_path('../../javattt/ttt.jar', __FILE__)
include_class Java::Javattt.AIPlayer
include_class Java::Javattt.Board
include_class Java::Javattt.Side


Java::Javattt.Side.class_eval do
	field_reader :X, :O, :_
end

class Solver
	X = Java::Javattt.Side.X
	O = Java::Javattt.Side.O
	Empty = Java::Javattt.Side._

	X_WINS = "Player X has won!"
	O_WINS = "Player O has won!"
	DRAW = "The game ended in a draw."

	def initialize(num_players)
		@num_players = num_players
	end

	def solve(game, side)
		side = java_side side
		out = {}
		player = AIPlayer.new side
		board = make_board game
		move = player.calculateMove(board)

		if move.nil?
			out["move"] = nil
		elsif @num_players == 1
			out["move"] = [move[0], move[1]]
			board.setCell(move[0], move[1], side)
		end

		winner = board.winner 
		if winner == X
			out["outcome"] = X_WINS
		elsif winner == O
			out["outcome"] = O_WINS
		elsif board.isDraw
			out["outcome"] = DRAW
		else 
			out["outcome"] = nil
		end

		out
	end

	def java_side(side)
		if side == "X"
			X
		elsif side == "O"
			O
		else
			Empty
		end
	end

	def make_board(rows)
		java_rows = rows.map do |row|
			java_row = row.map {|side| java_side side}
		end.to_java(Java::Javattt.Side[])

		Board.new java_rows
	end
end