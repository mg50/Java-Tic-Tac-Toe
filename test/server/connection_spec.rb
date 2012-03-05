require '../../src/server/connection'


describe Connection, "#initialize" do
	after :each do
		Connection.clear_all
	end

	it "creates a new session" do
		id = Connection.register
		conn = Connection[id]

		id.should == 1
		Connection[id].class.should == Connection
	end

	it "creates increasing session id's" do
		id1 = Connection.register
		id2 = Connection.register

		id1.should == 1
		id2.should == 2

		Connection[id1].class.should == Connection
		Connection[id2].class.should == Connection

		Connection[id1].should_not == Connection[id2]
	end

	it "creates a game for each connection" do
		id = Connection.register
		game = Connection[id].game

		game.class.should == HTTPGame
		game.state.class.should == Java::Javattt::fsm.NewGameState
	end
end