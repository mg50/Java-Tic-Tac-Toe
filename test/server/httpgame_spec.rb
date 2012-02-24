require 'rubygems'
require 'json'
require 'java'
require '../../src/server/httpgame'
require '../../src/server/HTTPUI'


describe HTTPGame do
	it "initializes in the newGame stage" do
		HTTPGame.new.stage.should == Java::Javattt.Stage::newGame
	end

	it "returns valid JSON about its state" do
		time = Time.now.to_i
		game = HTTPGame.new
		game_info = JSON.parse game.status
		game_info['timestamp'].should == time
		game_info['stage'].should == "newGame"
		game_info['board'].should == [["", "", ""], ["", "", ""], ["", "", ""]]
	end
end