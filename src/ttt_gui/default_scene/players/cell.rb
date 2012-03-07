require '../javattt/ttt.jar'
include_class Java::Javattt.Board
include_class Java::Javattt.AIPlayer

module TicTacToeButton
	def self.extended(prop)

	end

	def mouse_clicked(e)
		num = id.to_i
		coords = [num / 3, num % 3]
		self.text = "clicked"
		puts "b"
	end
end