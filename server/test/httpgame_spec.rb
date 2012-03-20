require 'rubygems'
require 'json'
require 'java'
require File.join(File.dirname(__FILE__), '../src/httpgame')
require File.join(File.dirname(__FILE__), '../src/httpplayer')

include_class Java::Javattt.Side
include_class Java::Javattt.Board
include_class Java::Javattt.Player
include_class Java::Javattt::fsm.MoveState
include_class Java::Javattt::fsm.PlayVsAIState
include_class Java::Javattt::fsm.PlayAsXState
include_class Java::Javattt::fsm.BeginningGameState
include_class Java::Javattt::fsm.StartNewGameState
include_class Java::Javattt::strategy.HumanStrategy
include_class Java::Javattt::strategy.AIStrategy

describe HTTPGame do
	before :each do
		HTTPGame.clear_all_games
		HTTPPlayer.clear_all_players
	end

	it "refers to a game by room" do
		game = HTTPGame["room"]
		game.class.should == HTTPGame
	end

	it "does not create duplicates by room" do
		game1 = HTTPGame["room"]
		game2 = HTTPGame["room"]

		game1.should == game2
	end

	it "refers to a game by player" do
		player1 = HTTPPlayer[{:session_id => 1}]
		HTTPGame["room1"].add_player player1

		player2 = HTTPPlayer[{:session_id => 2}]
		HTTPGame["room2"].add_player player2

		HTTPGame[player1].should == HTTPGame["room1"]
		HTTPGame[player2].should == HTTPGame["room2"]
	end

	it "returns nil if it can't find a player's game" do
		player = HTTPPlayer[{:session_id => 1}]
		HTTPGame[player].should be_nil
	end

	it "removes a player from one game before adding to another" do
		g1 = HTTPGame["room1"]
		g2 = HTTPGame["room2"]
		p = HTTPPlayer[{:session_id => 1}]

		g1.add_player p
		g1.receive_signal p, "YES"
		g1.masterPlayer.should == p
		HTTPGame[p].should == g1
		g1.state.class.should == PlayAsXState
		g2.state.class.should == NewGameState

		g2.add_player p
		g2.masterPlayer.should == p
		g1.masterPlayer.should == nil
		HTTPGame[p].should == g2
		g2.state.class.should == PlayVsAIState
	end

	it "begins a game after a player is added" do
		player = HTTPPlayer[{:session_id => 1}]
		game = HTTPGame["room"]
		game.add_player player

		game.masterPlayer.should == player
		game.client_player.should be_nil
		game.state.class.should == PlayVsAIState
	end

	it "adds a client player" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		HTTPGame[p1].should == game
		HTTPGame[p2].should == game
		game.masterPlayer.should == p1
		game.client_player.should == p2
	end

	it "shouldn't add a third player" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		p3 = HTTPPlayer[{:session_id => 3}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2
		game.add_player p3

		HTTPGame[p3].should be_nil
	end

	it "should begin a game" do
		p1 = HTTPPlayer[{:session_id => 1}]
		game = HTTPGame["room"]
		game.add_player p1

		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"

		game.currentPlayer.should == p1
		game.state.class.should == MoveState
	end

	it "lets a player make a move against another" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p1.side = Side.X
		p2 = HTTPPlayer[{:session_id => 2}]
		p2.side = Side.O
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		game.state = MoveState.new game
		game.playerX = game.masterPlayer
		game.playerO = p2
		game.currentPlayer = game.playerX

		game.receive_signal p1, "MOVE", "coords" => [2, 2]

		game.get_ruby_grid[2][2].should == "X"
		game.currentPlayer.should == game.playerO
	end

	it "waits for the second player to join" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1

		game.receive_signal p1, "NO"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"

		game.state.class.should == BeginningGameState
		game.start
		game.state.class.should == BeginningGameState

		game.add_player p2
		game.state.class.should == MoveState
	end

	it "restarts when the client leaves in a middle of a 2-player game" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		game.receive_signal p1, "NO"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"

		game.receive_signal p2, "EXIT"
		game.client_player.should be_nil
		game.state.class.should == PlayVsAIState
		p1.ui.alert_message.should_not be_nil
	end

	it "restarts when the main player leaves and makes the client the master player" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		game.receive_signal p1, "NO"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"

		game.receive_signal p1, "EXIT"

		game.masterPlayer.should == p2
		game.client_player.should be_nil
		game.state.class.should == PlayVsAIState
		p2.ui.alert_message.should_not be_nil
	end		

	it "plays against the AI and then against a player" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		game.receive_signal p1, "YES"
		game.receive_signal p1, "NO"
		game.receive_signal p1, "YES"

		game.playerO.should == p1
		game.playerX.gameStrategy.class.should == AIStrategy
		game.receive_signal p1, "MOVE", "coords" => [2, 0]
		game.receive_signal p1, "MOVE", "coords" => [2, 1]
		game.state.class.should == StartNewGameState

		game.receive_signal p1, "YES"
		game.receive_signal p1, "NO"
		game.receive_signal p1, "NO"
		game.receive_signal p1, "YES"

		game.state.class.should == MoveState
		game.currentPlayer.should == p2
	end

	it "plays against another player and then against an AI" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		game.receive_signal p1, "NO"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"

		game.playerX.should == p1
		game.playerO.should == p2
		game.receive_signal p1, "MOVE", "coords" => [0, 0]
		game.receive_signal p2, "MOVE", "coords" => [2, 0]
		game.receive_signal p1, "MOVE", "coords" => [0, 1]
		game.receive_signal p2, "MOVE", "coords" => [2, 1]
		game.receive_signal p1, "MOVE", "coords" => [0, 2]
		game.state.class.should == StartNewGameState

		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"

		game.state.class.should == MoveState
		game.playerO.gameStrategy.class.should == AIStrategy

	end

	it "doesn't let a player move outside of his turn" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		game.receive_signal p1, "NO"
		game.receive_signal p1, "YES"
		game.receive_signal p1, "YES"

		game.receive_signal p1, "MOVE", "coords" => [0, 0]
		game.receive_signal p1, "MOVE", "coords" => [1, 1]

		game.currentPlayer.should == game.playerO
		game.board.getCell(0, 0).should == Side.X
		game.board.getCell(1, 1).should == Side._
	end

	it "alerts a player if the game owner restarts" do
		p1 = HTTPPlayer[{:session_id => 1}]
		p2 = HTTPPlayer[{:session_id => 2}]
		game = HTTPGame["room"]
		game.add_player p1
		game.add_player p2

		game.receive_signal p1, "RESTART"
		p2.ui.alert_message.should_not be_nil		
	end

	# it "returns valid JSON about its state" do
	# 	time = Time.now.to_i
	# 	game = HTTPGame.new
	# 	game_info = game.status
	# 	game_info['timestamp'].to_i.should == time.to_i
	# 	game_info['state'].should == "NewGameState"
	# 	game_info['board'].should == [["", "", ""], ["", "", ""], ["", "", ""]]
	# end

	# it "should initialize with a UI" do
	# 	game = HTTPGame.new
	# 	game.ui.class.should == HTTPUI
	# end


end