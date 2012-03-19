include_class Java::Javattt.Board
include_class Java::Javattt.command.MoveCommand
include_class Java::Javattt.fsm.MoveState

module Cell
	def mouse_clicked(e)
		return unless Game.state.is_a? MoveState and Game.currentPlayer == Game.masterPlayer

		size = id.split("_")[1].split("x")[0].to_i
		num = id.split("_")[0].to_i
		coords = [num % size, num / size].to_java :int


		Game.state = Java::Javattt.fsm.MoveState.new Game
		cmd = MoveCommand.new(coords)
		Game.start cmd
	end
end