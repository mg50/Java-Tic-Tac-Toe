require '../src/app'

describe App, "#solve" do
	it "plays top left on an empty board" do
		game_string = "000000000"
		solve(000000000, "x").should eq([0, 0])
	end

	it "correctly makes a three-in-a-row when possible" do
		game_string = "oo0xx0000"
		solve(game_string, "x").should eq([2, 1])
		solve(game_string, "o").should eq([2, 0])
	end

	it "returns nil on a draw" do
		game_string = "xoxoxoxox"
		solve(game_string, "x").should be_nil
	end

	it "forks when it can" do
		game_string = "o0x0o0x00"
		solve(game_string, "x").should eq([2, 2])
	end
end