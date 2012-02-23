TEST = true

require '../../src/server/app'
require 'net/http'
require 'json'


def get_req(path)
	json = Net::HTTP.get('localhost', path)
	yield JSON.parse json
end

def post_req(path, params)
	uri = URI('http://localhost' + path)
	json = Net::HTTP.post_form(uri, params)
	yield JSON.parse json
end

describe App, "" do
	after :each do
		send_req('/clear_all_data')
	end

	it "starts a new game and gives me an id" do
		get_req('/') do |resp|
			resp.sessionId.should == 1
		end
	end

	it "wants to know if you'd like to play vs. an AI" do
		get_req('/')
		get_req('/status') do |resp|
			resp.stage.should == 'receivingAIResponse'
		end
	end

	it "understands a query to play against an AI" do
		get_req('/')
		send_req('/query', :response => true)
		get_req('/status') do |resp|
			resp.stage.should == 'receivingPlayAsX'
	end

	it "makes the first move as an AI" do
		get_req('/')
		send_req('/query', "response" => true)
		get_req('/query', "response" => false)
		get_req('/status') do |resp|
			resp.status.should == 'receivingMove'
			resp.board.should == [["X", "", ""], ["", "", ""], ["", "", ""]]
		end
	end

	it "plays the second move as O" do
		get_req('/')
		send_req('/query', "response" => true)
		get_req('/query', "response" => true)
		get_req('/status') do |resp|
			resp.status.should == 'receivingMove'
			resp.board.should == [["", "", ""], ["", "", ""], ["", "", ""]]
		end
		send_req('/query', "move" => [2, 2])
		get_req('/status') do |resp|
			resp.status.should == 'receivingMove'
			resp.board.should == [["", "", ""], ["", "O", ""], ["", "", "X"]]
		end
	end

	it "plays the third move as X" do
		get_req('/')
		send_req('/query', "response" => true)
		get_req('/query', "response" => false)
		send_req('/query', "move" => [1, 1])
		get_req('/status') do |resp|
			resp.status.should == 'receivingMove'
			resp.board.should == [["X", "", ""], ["X", "O", ""], ["", "", ""]]
		end
	end

	it "plays ignores moves on empty squares" do
		get_req('/')
		send_req('/query', "response" => true)
		get_req('/query', "response" => true)
		send_req('/query', "move" => [2, 0])
		send_req('/query', "move" => [2, 0])

		get_req('/status') do |resp|
			resp.status.should == 'receivingMove'
			resp.board.should == [["", "", "X"], ["", "O", ""], ["", "", ""]]
		end
	end		
end






=begin
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
=end