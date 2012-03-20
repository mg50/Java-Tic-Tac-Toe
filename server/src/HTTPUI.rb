
include_class Java::Javattt::ui.UI
include_class Java::Javattt.command::PauseCommand

class HTTPUI
	include UI

	attr_accessor :message, :alert_message, :wait_message

	def initialize
	end


	def update(board)
	end

	def promptMove(board)
		PauseCommand.new
	end

	def prompt(message)
		PauseCommand.new
	end


	def victoryMessage(message, xWinsCount, oWinsCount)
		self.alert_message = message
	end

	def displayHelp
	end

	def displayMessage(msg)
		self.message = msg
		PauseCommand.new
	end
end