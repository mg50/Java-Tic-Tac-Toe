
include_class Java::Javattt.UI
include_class Java::Javattt.command::PauseCommand

class HTTPUI
	include UI

	attr_accessor :message

	def initialize
	end


	def update(board)
	end

	def promptMove(board)
		PauseCommand.new
	end

	def promptPlay3x3
		PauseCommand.new
	end

	def promptPlayAsX
		PauseCommand.new
	end

	def promptPlayVsAI
		PauseCommand.new
	end

	def promptStartNewGame
		PauseCommand.new
	end

	def victoryMessage(victor, xWinsCount, oWinsCount)
	end

	def displayHelp
	end

	def displayMessage(msg)
		self.message = msg
		PauseCommand.new
	end
end