require '../../src/server/room'
include_class Java::Javattt::fsm.WaitingState
include_class Java::Javattt::fsm.ReceivingMoveState
include_class Java::Javattt::fsm.ReceivingStartNewGameState
include_class Java::Javattt::fsm.GameOverState

describe Room do

	after :each do
		Room.destroy_all_rooms
	end
	
	it "is referred to by name" do
		room1 = Room.new "room1"
		room2 = Room.new "room2"

		Room["room1"].should == room1
		Room["room2"].should == room2
	end

	it "doesn't create duplicate rooms" do
		room = Room.new "room"
		duplicate_room = Room.new "room"

		Room["room"].should == room
	end

	it "creates a room automatically" do
		room = Room["room"]

		room.class.should == Room
	end

	it "sets up a game between two players before game options are chosen" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		g1.start
		g2.start
		
		room.add_game g1
		room.add_game g2

		g2.state.class.should == Java::Javattt::fsm.WaitingState

		g1.receive_signal "signal" => "YES" # play 3x3 game
		g1.receive_signal "signal" => "NO" # play 2-player

		g1.state.class.should == ReceivingMoveState
		g2.state.class.should == ReceivingMoveState

		g2.currentPlayer.should == (g1.currentPlayer == g1.playerX ? g2.playerX : g2.playerO)
	end

	it "sets up a game between two players after game options are chosen" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		g1.start
		g2.start
		
		room.add_game g1

		g1.receive_signal "signal" => "YES" # play 3x3 game
		g1.receive_signal "signal" => "NO" # play 2-player

		room.add_game g2

		g1.state.class.should == ReceivingMoveState
		g2.state.class.should == ReceivingMoveState

		g2.currentPlayer.should == (g1.currentPlayer == g1.playerX ? g2.playerX : g2.playerO)
	end

	it "makes the second player wait for the first after a game ends" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		room.add_game g1
		room.add_game g2

		g1.state = GameOverState.new g1
		g2.duplicate_game_state g1

		g1.start
		
		g1.state.class.should == ReceivingStartNewGameState
		g2.state.class.should == WaitingState
	end

	it "lets two players play multiple games in a row" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		room.add_game g1
		room.add_game g2

		g1.state = GameOverState.new g1
		g2.duplicate_game_state g1

		g1.start
		
		g1.state.class.should == ReceivingStartNewGameState
		g2.state.class.should == WaitingState

		g1.receive_signal "signal" => "YES" # play again
		g1.receive_signal "signal" => "YES" # play 3x3
		g1.receive_signal "signal" => "NO" # play vs other player

		g1.state.class.should == ReceivingMoveState
		g2.state.class.should == ReceivingMoveState
		
		g1.opponent_game.should == g2
		g2.opponent_game.should == g1
	end

	it "makes other players wait while a room owner plays against the AI" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		room.add_game g1
		room.add_game g2

		g1.start

		g1.receive_signal "signal" => "YES" # play again
		g1.receive_signal "signal" => "YES" # play 3x3
		g1.receive_signal "signal" => "YES" # play vs other player

		g1.state.class.should == ReceivingMoveState
		g2.state.class.should == WaitingState		
	end


	it "cedes ownership to the second player when the first leaves" do
		room = Room["room"]

		g1 = HTTPGame.new
		g2 = HTTPGame.new
		room.owner = g1
		room.player = g2

		room.remove_game g1

		room.owner.should == g2
		room.player.should be_nil
	end

	it "removes a game when no players are left" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		room.owner = g1
		room.player = g2

		room.remove_game g1
		room.remove_game g2

		Room["room"].should_not == room
	end

	it "creates a random room" do
		room1 = Room.create_empty_room
		room2 = Room.create_empty_room

		room1.class.should == Room
		room2.class.should == Room

		room1.name.should_not be_nil
		room2.name.should_not be_nil

		room1.name.should_not == room2.name
	end

	it "gives the owner control over the game settings" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		room.add_game g1
		g1.start
		
	end

end