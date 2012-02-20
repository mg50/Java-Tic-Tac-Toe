require '../../src/server/solver'


describe Solver, "#make_grid" do
	it "correctly interprets game strings" do
		game = [["X", "O", "X"], ["", "", ""], ["O", "X", "O"]]
		board = Solver.new(1).make_board(game)

		board.getCell(0, 0).should eq(Solver::X)
		board.getCell(1, 0).should eq(Solver::O)
		board.getCell(2, 0).should eq(Solver::X)

		board.getCell(0, 1).should eq(Solver::Empty)
		board.getCell(1, 1).should eq(Solver::Empty)
		board.getCell(2, 1).should eq(Solver::Empty)

		board.getCell(0, 2).should eq(Solver::O)
		board.getCell(1, 2).should eq(Solver::X)
		board.getCell(2, 2).should eq(Solver::O)
	end
end

describe Solver, "#solve" do
	it "plays top left on an empty board" do
		game = [["", "", "",], ["", "", "",], ["", "", "",]]
		Solver.new(1).solve(game, "X")['move'].should eq([0, 0])
	end

	it "correctly makes a three-in-a-row when possible" do
		game = [["O", "O", ""], ["X", "X", ""], ["", "", ""]]
		response1 = Solver.new(1).solve(game, "X")
		response1['move'].should eq([2, 1])
		response1['outcome'].should eq(Solver::XWinsMessage)


		response2 = Solver.new(1).solve(game, "O")
		response2['move'].should eq([2, 0])
		response2['outcome'].should eq(Solver::OWinsMessage)
	end

	it "returns nil on a draw" do
		game = [["X", "O", "X"], ["O", "X", "O"], ["X", "O", "X"]]
		Solver.new(1).solve(game, "X")['move'].should be_nil
	end

	it "forks when it can" do
		game = [["O", "", "X"], ["", "O", ""], ["X", "", ""]]
		Solver.new(1).solve(game, "X")['move'].should eq([2, 2])
	end

	it "recognizes a draw" do
		game = [["X", "O", "O"], ["O", "O", "X"], ["X", "X", "O"]]
		response = Solver.new(2).solve(game, "O")
		response['move'].should be_nil
		response['outcome'].should eq(Solver::DrawMessage)
	end

end