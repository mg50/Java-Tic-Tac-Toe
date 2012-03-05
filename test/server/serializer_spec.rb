
require '../../src/server/serializer'


describe Serializer, "#game_hash" do
	serializer = Serializer.new
	game = nil

	before :each do
		game = HTTPGame.new
	end

	it "converts a game into a hash" do
		hash = serializer.game_hash game
		hash.should == {:state => "NewGameState", :board => [["","",""]]*3, 
						:currentPlayer => nil, :playerX => nil, :playerO => nil,
						:room => nil, :waiting_for_opponent => nil}
	end

	it "converts a game into a hash (2)" do
		game.start

		hash = serializer.game_hash game
		hash.should == {:state => "ReceivingPlay3x3State", :board => [["","",""]]*3, 
						:currentPlayer => nil, :playerX => nil, :playerO => nil,
						:room => nil, :waiting_for_opponent => false}
	end

	it "converts a game into a hash (3)" do
		game.start
		game.receive_signal "signal" => "YES"

		hash = serializer.game_hash game
		hash.should == {:state => "ReceivingPlayVsAIState", :board => [["","",""]]*3, 
						:currentPlayer => nil, :playerX => nil, :playerO => nil,
						:room => nil, :waiting_for_opponent => false}
	end

	it "converts a game into a hash (4)" do
		game.start
		game.receive_signal "signal" => "NO"

		hash = serializer.game_hash game
		hash.should == {:state => "ReceivingPlayVsAIState", :board => [["","","",""]]*4, 
						:currentPlayer => nil, :playerX => nil, :playerO => nil,
						:room => nil, :waiting_for_opponent => false}
	end

	it "converts a game into a hash (5)" do
		game.start
		game.receive_signal "signal" => "NO"
		game.receive_signal "signal" => "YES"

		hash = serializer.game_hash game
		hash.should == {:state => "ReceivingPlayAsXState", :board => [["","","",""]]*4, 
						:currentPlayer => nil, :playerX => nil, :playerO => nil,
						:room => nil, :waiting_for_opponent => false}
	end	

	it "converts a game into a hash (6)" do
		game.start
		game.receive_signal "signal" => "NO"
		game.receive_signal "signal" => "NO"

		hash = serializer.game_hash game
		hash.should == {:state => "ReceivingMoveState", :board => [["","","",""]]*4, 
						:currentPlayer => "X", :playerX => "HumanPlayer", :playerO => "HumanPlayer",
						:room => nil, :waiting_for_opponent => true}
	end

	it "converts a game into a hash (7)" do
		game.start
		game.receive_signal "signal" => "NO"
		game.receive_signal "signal" => "YES"
		game.receive_signal "signal" => "YES"

		hash = serializer.game_hash game
		hash.should == {:state => "ReceivingMoveState", :board => [["","","",""]]*4, 
						:currentPlayer => "X", :playerX => "HumanPlayer", :playerO => "AIPlayer",
						:room => nil, :waiting_for_opponent => false}
	end

	it "converts a game into a hash (8)" do
		game.start
		game.receive_signal "signal" => "YES"
		game.receive_signal "signal" => "YES"
		game.receive_signal "signal" => "NO"

		hash = serializer.game_hash game
		hash.should == {:state => "ReceivingMoveState", :board => [["X","",""],["","",""],["","",""]],
						:currentPlayer => "O", :playerX => "AIPlayer", :playerO => "HumanPlayer",
						:room => nil, :waiting_for_opponent => false}
	end
end


describe Serializer, "#revive" do
	serializer = Serializer.new
	game = nil

	before :each do
		game = HTTPGame.new
	end

	it "revives a game from a hash" do
		hash = {:state => "NewGameState", :board => [["","",""]]*3, 
				:currentPlayer => nil, :playerX => nil, :playerO => nil,
				:room => nil, :waiting_for_opponent => false}
		
		game = serializer.revive hash
		game.state.class.should == Java::Javattt::fsm.NewGameState
		game.get_ruby_grid.should == [["", "", ""]]*3
		game.currentPlayer.should be_nil
		game.playerX.should be_nil
		game.playerO.should be_nil
		game.room.should be_nil
	end

	it "revives a game from a hash (2)" do

		hash = {:state => "ReceivingMoveState", :board => [["X","",""],["","",""],["","",""]],
						:currentPlayer => "O", :playerX => "AIPlayer", :playerO => "HumanPlayer",
						:room => nil, :waiting_for_opponent => false}
		game = serializer.revive hash

		game.state.class.should == Java::Javattt::fsm.ReceivingMoveState
		game.get_ruby_grid.should == [["X","",""],["","",""],["","",""]]
		game.playerX.class.should == Java::Javattt.AIPlayer
		game.playerO.class.should == Java::Javattt.HumanPlayer
		game.currentPlayer.should == game.playerO
	end
end

describe Serializer, "#deserialize_all" do
	serializer = Serializer.new
	game = nil

	it "revives multiple games" do
		game1 = HTTPGame.new
		game2 = HTTPGame.new
		game3 = HTTPGame.new

		game1.room = "room1"
		game2.room = "room2"

		serializer.serialize_and_save [game1,game2,game3]
		g1, g2, g3 = serializer.deserialize_all

		g1.room.should == "room1"
		g2.room.should == "room2"
		g3.room.should be_nil

		[g1, g2, g3].each do |game|
			game.class.should == HTTPGame
		end
	end
end