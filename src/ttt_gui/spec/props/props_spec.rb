require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")


describe "Props" do
	include PropSpecHelper
	before :each do
		setup_prop_test
	end

	it "should have nine cells" do
		(0..9).each do |i|
			@scene.find(i.to_s).should_not be_nil
		end
	end
end