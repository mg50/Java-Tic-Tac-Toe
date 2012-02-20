require 'rubygems'
require 'json'
require 'java'
require File.expand_path('../../javattt/ttt.jar', __FILE__)
include_class Java::Javattt.AIPlayer
include_class Java::Javattt.Board


Java::Javattt.Board.class_eval do
	field_reader :X, :O, :Empty
end

class Solver
	X = Java::Javattt.Board.X
	O = Java::Javattt.Board.O
	Empty = Java::Javattt.Board.Empty

	XWinsMessage = "Player X has won!"
	OWinsMessage = "Player O has won!"
	DrawMessage = "The game ended in a draw."

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
			out["outcome"] = XWinsMessage
		elsif winner == O
			out["outcome"] = OWinsMessage
		elsif board.isDraw
			out["outcome"] = DrawMessage
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
		end.to_java(Java::int[])

		Board.new java_rows
	end
end