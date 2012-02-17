require '../src/app'


describe Solver, "#make_grid" do
	it "correctly interprets game strings" do
		game_string = "xox000oxo"
		board = Solver.new.make_board(game_string)

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
		game_string = "000000000"
		Solver.new.solve(game_string, "x").should eq([0, 0])
	end

	it "correctly makes a three-in-a-row when possible" do
		game_string = "oo0xx0000"
		Solver.new.solve(game_string, "x").should eq([2, 1])
		Solver.new.solve(game_string, "o").should eq([2, 0])
	end

	it "returns nil on a draw" do
		game_string = "xoxoxoxox"
		Solver.new.solve(game_string, "x").should be_nil
	end

	it "forks when it can" do
		game_string = "o0x0o0x00"
		Solver.new.solve(game_string, "x").should eq([2, 2])
	end

end