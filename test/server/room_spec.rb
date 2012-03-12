require '../../src/server/room'

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

	it "cedes ownership to the second player when the first leaves" do
		room = Room["room"]

		g1 = HTTPGame.new
		g2 = HTTPGame.new
		room.owner = g1
		room.player = g2

		room.on_leave g1

		room.owner.should == g2
		room.player.should be_nil
	end

	it "removes a game when no players are left" do
		room = Room["room"]
		g1 = HTTPGame.new
		g2 = HTTPGame.new

		room.owner = g1
		room.player = g2

		room.on_leave g1
		room.on_leave g2

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
end