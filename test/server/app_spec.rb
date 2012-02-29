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
end