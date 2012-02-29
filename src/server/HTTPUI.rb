
include_class Java::Javattt.UI
include_class Java::Javattt.TransitionData::Signal

class HTTPUI
	include UI

	def initialize
	end


	def update(board)
	end

	def promptMove(board)
		Java::Javattt.TransitionData.new(Java::Javattt.TransitionData::Signal::PAUSE)
	end

	def promptPlay3x3
		Java::Javattt.TransitionData.new(Java::Javattt.TransitionData::Signal::PAUSE)
	end

	def promptPlayAsX
		Java::Javattt.TransitionData.new(Java::Javattt.TransitionData::Signal::PAUSE)
	end

	def promptPlayVsAI
		Java::Javattt.TransitionData.new(Java::Javattt.TransitionData::Signal::PAUSE)
	end

	def promptStartNewGame
		Java::Javattt.TransitionData.new(Java::Javattt.TransitionData::Signal::PAUSE)
	end

	def victoryMessage(victor, xWinsCount, oWinsCount)
	end
end