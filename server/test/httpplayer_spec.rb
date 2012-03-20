require 'rubygems'
require 'json'
require 'java'
require File.join(File.dirname(__FILE__), '../src/httpgame')
require File.join(File.dirname(__FILE__), '../src/httpplayer')


describe HTTPPlayer do

	it "is referred to by session" do
		session = {:session_id => 4}
		HTTPPlayer[session].class.should == HTTPPlayer
		HTTPPlayer[session].ui.class.should == HTTPUI
		HTTPPlayer[session].gameStrategy.class.should == HumanStrategy
		HTTPPlayer[session].timestamp.should == Time.now.to_i
	end

	it "doesn't create duplicates of the same player" do
		session1 = {:session_id => 4}
		session2 = {:session_id => 4}
		HTTPPlayer[session1].should == HTTPPlayer[session2]
	end

	it "returns its UI's status" do
		player = HTTPPlayer.new 1
		player.alert_message "Alert"
		player.wait_message "Wait"

		player.ui_status.should == {:alert_message => "Alert", :wait_message => "Wait"}
	end
end