require 'pry'
include_class Java::Javattt.command.PauseCommand
include_class Java::Javattt.Side

Side.class_eval do
	field_reader :X, :O
end


class LimelightUI
	include Java::Javattt.ui.UI

	attr_accessor :scene, :board_3x3, :board_4x4, :current_board

	def initialize
	end

	def load_boards
		@board_3x3 = @scene.find_by_name("board_3x3")[0]
		@board_4x4 = @scene.find_by_name("board_4x4")[0]
		@current_board = @board_3x3

		@container = @scene.find_by_name("board_container")[0]
		@container.remove @board_4x4
	end

	def load_current_board
		@container.remove @board_3x3
		@container.remove @board_4x4
		@container.add @current_board
	end

	def limelight_marker(java_marker)
		case java_marker
		when Side.X
			"X"
		when Side.O
			"O"
		else
			" "
		end
	end

	def update(board)
		size = board.size
		@current_board.children.each do |cell|
			id = cell.id.split("_")[0].to_i
			x = id % size
			y = id / size

			cell.text = limelight_marker board.getCell(x, y)
		end
	end

	def promptMove(board)
		PauseCommand.new
	end

	def prompt(message)
		scene.find("message_prompt").text = message
		
		scene.find_by_name("sidebar")[0].children.each do |c|
			c.style.transparency = "0%"
		end

		PauseCommand.new
	end

	def set_prompt_options(yes, no)
		scene.find("button_yes").text = yes
		scene.find("button_no").text = no
	end

	def victoryMessage(victor, xWinsCount, oWinsCount)
	end

	def displayMessage(message)
	end

	def displayHelp
	end

end