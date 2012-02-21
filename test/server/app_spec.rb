TEST = true

require '../../src/server/app'

describe App, "#respond_to_ttt_request"  do
	it "moves on a blank board" do
		game_string = '[["","",""],["","",""],["","",""]]'
		num_players = "1"
		json = '{"success":true,"move":[0,0],"outcome":null}'

		App.respond_to_ttt_request(game_string, num_players).should == json
	end

	it "recommends a move" do
		game_string = '[["X","","O"],["","",""],["","",""]]'
		num_players = "1"
		json = '{"success":true,"move":[0,1],"outcome":null}'

		App.respond_to_ttt_request(game_string, num_players).should == json
	end

	it "recognizes a victory" do
		game_string ='[["O","O","O"],["X","X",""],["X","",""]]'
		num_players = "2"
		json = '{"success":true,"move":null,"outcome":"' + Solver::O_WINS + '"}'

		App.respond_to_ttt_request(game_string, num_players).should == json
	end

	it "recognizes a draw" do
		game_string = '[["X","O","X"],["X","O","O"],["O","","X"]]'
		num_players = "1"
		json = '{"success":true,"move":[1,2],"outcome":"' + Solver::DRAW + '"}'

		App.respond_to_ttt_request(game_string, num_players).should == json
	end

	it "handles bad JSON gracefully" do
		game_string = "[bad JSON]"
		num_players = "2"
		json = '{"success":false}'

		App.respond_to_ttt_request(game_string, num_players).should == json
	end
end