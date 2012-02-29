TEST = true

require '../../src/server/app'
require 'net/http'
require 'json'


describe App, "" do
	before :each do
		conn1 = Connection.register
		conn2 = Connection.register

		Connection.class_eval do
			@@connections = {1 => Connection[conn1], 2 => Connection[conn2]}
		end
	end

	after :each do
		Connection.clear_all
	end

	it "gives me my id when I connect" do
		(App.handshake 1)['id'] = 1
	end

	it "asks me to play vs. an AI" do
		App.handshake 1
		status = App.get_status 1
		status['stage'].should == 'receivingPlayVsAI'
	end

	it "accepts my response" do
		App.handshake 1
		App.query(1, {"signal" => true})
		status = App.get_status 1
		status['stage'].should == 'receivingPlayAsX'
	end

	it "accepts my response (2)" do
		App.handshake 1
		App.query(1, {"signal" => false})
		status = App.get_status 1
		status['stage'].should == 'receivingMove'
	end

	it "plays a full game" do
		App.handshake 1
		App.query(1, {"signal" => false})
		App.query 1, "signal" => "MOVE", "coords" => [0, 0]		
		App.query 1, "signal" => "MOVE", "coords" => [2, 0]		
		App.query 1, "signal" => "MOVE", "coords" => [0, 1]		
		App.query 1, "signal" => "MOVE", "coords" => [2, 1]		

		App.get_status(1)['stage'].should == 'receivingMove'

		App.query 1, "signal" => "MOVE", "coords" => [0, 2]		

		App.get_status(1)['stage'].should == 'receivingStartNewGame'
	end

	it "plays a full game against an AI" do
		App.handshake 1
		App.query 1, "signal" => true
		App.query 1, "signal" => false
		App.query 1, "signal" => "MOVE", "coords" => [2, 0]

		App.get_status(1)['stage'].should == 'receivingMove'

		App.query 1, "signal" => "MOVE", "coords" => [2, 1]

		App.get_status(1)['stage'].should == 'receivingStartNewGame'
	end
end