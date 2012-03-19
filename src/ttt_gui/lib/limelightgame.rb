include_class Java::Javattt.ui.MockUI

class LimelightGame < Java::Javattt.LocalGame
	attr_accessor :scene, :ui

	def scene=(scene)
		self.masterPlayer.ui.scene = scene
	end

	def initialize
		super
		self.masterPlayer.ui = LimelightUI.new
	end

	def onPlay3x3
		self.masterPlayer.ui.current_board = self.board.size == 3 ? self.masterPlayer.ui.board_3x3 :
																	self.masterPlayer.ui.board_4x4

		self.masterPlayer.ui.load_current_board
	end

	def load_boards
		self.masterPlayer.ui.load_boards
	end

	def begin(scene=nil)
  		Game.scene = scene
  		Game.load_boards
  		Game.start		
  	end
end

Game = LimelightGame.new