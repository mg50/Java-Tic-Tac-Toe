require 'rubygems'
require 'json'
require 'java'
require '../../src/server/httpgame'
include_class Java::Javattt.Stage

describe HTTPGame do
	def start_two_player_game(room1=nil, room2=nil)
		game1 = HTTPGame.new
		game2 = HTTPGame.new

		game1.room = room1
		game2.room = room2

		[game1, game2].each do |game|
			game.start nil
			game.receive_signal "signal" => "YES" # for 3x3
			game.receive_signal "signal" => "NO"  # for 2-player game
		end

		[game1, game2]
	end

	before :each do
		HTTPGame.clear_waiting_games
	end

	it "returns valid JSON about its state" do
		time = Time.now.to_i
		game = HTTPGame.new
		game_info = game.status
		game_info['timestamp'].should == time
		game_info['state'].should == "NewGameState"
		game_info['board'].should == [["", "", ""], ["", "", ""], ["", "", ""]]
	end

	it "should initialize with a UI" do
		game = HTTPGame.new
		game.ui.class.should == HTTPUI
	end

	it "plays a full game against the AI" do
		game = HTTPGame.new
		game.state.class.should == Java::Javattt::fsm.NewGameState

		game.start nil
		game.state.class.should == Java::Javattt::fsm.ReceivingPlay3x3State

		game.receive_signal "signal" => "YES"
		game.state.class.should == Java::Javattt::fsm.ReceivingPlayVsAIState

		game.receive_signal "signal" => "YES"
		game.state.class.should == Java::Javattt::fsm.ReceivingPlayAsXState

		game.receive_signal "signal" => "NO"
		game.state.class.should == Java::Javattt::fsm.ReceivingMoveState

		game.receive_signal "signal" => "MOVE", "coords" => [2, 0]
		game.receive_signal "signal" => "MOVE", "coords" => [2, 1]

		game.state.class.should == Java::Javattt::fsm.ReceivingStartNewGameState
	end

	it "sets up a game against two players" do
		game1 = HTTPGame.new
		game2 = HTTPGame.new

		game1.start nil
		game1.receive_signal "signal" => "YES"
		game1.receive_signal "signal" => "NO"

		game1.opponent_game.should be_nil
		game1.waiting_for_opponent.should be_true

		game2.start nil
		game2.receive_signal "signal" => "YES"
		game2.receive_signal "signal" => "NO"

		game1.opponent_game.should == game2
		game2.opponent_game.should == game1

		game1.waiting_for_opponent.should be_false
		game2.waiting_for_opponent.should be_true
	end

	it "plays a two-player game" do
		game1, game2 = start_two_player_game

		game1.receive_signal "signal" => "MOVE", "coords" => [0, 0]

		game2.receive_signal "signal" => "MOVE", "coords" => [2, 0]
		game1.receive_signal "signal" => "MOVE", "coords" => [0, 1]
		game2.receive_signal "signal" => "MOVE", "coords" => [2, 1]

		game1.state.class.should == Java::Javattt.fsm.ReceivingMoveState
		game2.state.class.should == Java::Javattt.fsm.ReceivingMoveState

		game1.receive_signal "signal" => "MOVE", "coords" => [0, 2]

		game1.state.class.should == Java::Javattt::fsm.ReceivingStartNewGameState
		game1.state.class.should == Java::Javattt::fsm.ReceivingStartNewGameState
	end

	it "should not let a player move on the wrong turn" do
		game1, game2 = start_two_player_game

		game1.receive_signal "signal" => "MOVE", "coords" => [0, 0]
		grid = game1.get_ruby_grid

		game1.receive_signal "signal" => "MOVE", "coords" => [0, 1]
		game1.get_ruby_grid.should == grid

		game2.receive_signal "signal" => "MOVE", "coords" => [0, 1]
		game2.get_ruby_grid.should_not == grid
	end

	it "should start a new game for a player if his opponent quits" do
		game1, game2 = start_two_player_game

		game2.receive_signal "signal" => "EXIT"
		game1.state.class.should == Java::Javattt::fsm.ReceivingStartNewGameState
	end

	it "should start a new game for a player if his opponent quits (2)" do
		game1, game2 = start_two_player_game

		game2.receive_signal "signal" => "EXIT"
		game1.state.class.should == Java::Javattt::fsm.ReceivingStartNewGameState
	end

	it "should allow you to play vs the AI after playing two-player" do
		game1, game2 = start_two_player_game
		game2.receive_signal "signal" => "EXIT"

		game1.receive_signal "signal" => "YES" # start a new game
		game1.receive_signal "signal" => "YES" # play 3x3 game
		game1.receive_signal "signal" => "YES" # play vs. AI
		game1.receive_signal "signal" => "YES" # play as X

		game1.two_player?.should be_false
		game1.state.class.should == Java::Javattt::fsm.ReceivingMoveState
		game1.waiting_for_opponent.should be_false

		game1.receive_signal "signal" => "MOVE", "coords" => [0, 0]
		game1.get_ruby_grid[0][0].should == "X"
	end

	it "shouldn't connect a 3x3 player with a 4x4 player" do
		game1 = HTTPGame.new
		game1.start nil
		game1.receive_signal "signal" => "YES"
		game1.receive_signal "signal" => "NO"
		game1.get_ruby_grid.length.should == 3

		game2 = HTTPGame.new
		game2.start nil
		game2.receive_signal "signal" => "NO"
		game2.receive_signal "signal" => "NO"
		game2.get_ruby_grid.length.should == 4

		game1.opponent_game.should be_nil
		game2.opponent_game.should be_nil
	end

	it "connects two people in the same room" do
		game1, game2 = start_two_player_game("my room", "my room")

		game1.opponent_game.should == game2
		game2.opponent_game.should == game1
	end

	it "doesn't connect people in different rooms" do
		game1, game2 = start_two_player_game("room1", "room2")

		game1.opponent_game.should be_nil
		game2.opponent_game.should be_nil
	end
end