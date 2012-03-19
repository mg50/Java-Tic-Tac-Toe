require 'pry'
require '../javattt/ttt.jar'
require 'java'
include_class Java::Javattt.LanguageStore
include_class Java::Javattt::fsm.NewGameState
include_class Java::Javattt::fsm.MoveState
include_class Java::Javattt::fsm.PlayVsAIState
include_class Java::Javattt::fsm.PlayAsXState
include_class Java::Javattt::command.YesCommand
include_class Java::Javattt::command.MoveCommand

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")


describe "Default Scene" do

	after :each do
		Game.state = NewGameState.new Game
	end

	uses_limelight :scene => "default_scene", :hidden => true

	it "contains two boards" do
  		scene.find_by_name("board_container")[0].children.should == scene.find_by_name("board_3x3") +
  		scene.find_by_name("board_4x4")
  	end

  	it "initializes with a 3x3 board" do
  		Game.begin(scene)
  		scene.find_by_name("board_container")[0].children.should == scene.find_by_name("board_3x3")  		
  	end

  	it "displays a prompt" do
  		Game.begin(scene)
  		scene.find("message_prompt").text.should == LanguageStore.instance.PROMPT_PLAY_VS_AI
  	end

  	it "responds to button click" do
  		Game.begin(scene)  	
  		Game.state.class.should == PlayVsAIState

  		scene.find("button_yes").mouse_clicked(nil)

  		Game.state.class.should == PlayAsXState	
  	end

  	it "hides the sidebar once the game starts" do
  		Game.begin(scene)

  		scene.find("button_yes").mouse_clicked(nil)
  		scene.find("button_yes").mouse_clicked(nil)
  		scene.find("button_yes").mouse_clicked(nil)

  		scene.find_by_name("sidebar")[0].children.each do |child|
  			child.style.transparency.should == "100%"
  		end
  	end

  	it "switches boards" do
  		Game.begin(scene)

  		scene.find("button_yes").mouse_clicked(nil)
  		scene.find("button_yes").mouse_clicked(nil)

  		scene.find_by_name("board_container")[0].children.should == scene.find_by_name("board_3x3")

  		scene.find("button_no").mouse_clicked(nil)

  		scene.find_by_name("board_container")[0].children.should == scene.find_by_name("board_4x4")  		
  	end  	

	it "updates the board after clicking a square" do
		Game.begin(scene)
		Game.start YesCommand.new
		Game.start YesCommand.new
		Game.start YesCommand.new

		Game.state.class.should == MoveState

		scene.find("4_3x3").text.should == " "
		scene.find("4_3x3").mouse_clicked(nil)
		scene.find("4_3x3").text.should == "X"
	end
end