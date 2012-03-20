require 'rubygems'
require 'sinatra'
require 'json'
require 'pry'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'httpgame')
require File.join(File.dirname(__FILE__), 'httpplayer')
require File.join(File.dirname(__FILE__), 'httpui')
require File.join(File.dirname(__FILE__), 'serializer')
include_class Java::Javattt::command.RestartCommand
include_class Java::Javattt::fsm.HaltState
TEST = false unless defined? TEST

class App < Sinatra::Base
	enable :sessions
	set :server, ["mongrel"]

	all_previous_games = Serializer.new.deserialize_all || []
	ip_hash = {}
	all_previous_games.each do |game|
		#ip_hash[game.ip] = game
	end

	get '/' do
		redirect '/' + HTTPGame.random_empty_room_name
	end

	get '/favicon.ico' do
	end

	get '/test' do
		File.read(File.join(File.dirname(__FILE__), './public/html/test.html'));
	end	

	get '/:room' do
#		unless session[:id]
#			session[:id] = Connection.register(request.ip) 			
#			if saved_game = ip_hash[@env['REMOTE_ADDR']] and saved_game.room == params[:room]
#				Connection[session[:id]].game = saved_game
#			end
#		end

#		game = Connection[session[:id]].game

		player = HTTPPlayer[session]
		game = HTTPGame[params[:room]]

		game.add_player player unless HTTPGame[player] == game
		if game.state.is_a? HaltState
			game.receive_signal player, "signal" => "RESTART"
		end		

		File.read(File.join(File.dirname(__FILE__), './public/html/index.html'));
	end

	post '/' do
		JSON.generate "id" => session[:session_id], "timestamp" => Time.now.to_i
	end

	post '/status' do
		player = HTTPPlayer[session]
		JSON.generate HTTPGame[player].status player if HTTPGame[player]
	end

	post '/query' do
		player = HTTPPlayer[session]
		game = HTTPGame[player]
		
		game.receive_signal(player, params[:signal], JSON.parse(params[:options])) if game
		#Serializer.new.serialize_and_save
		JSON.generate "response" => true
	end
end

App.run! :host => 'localhost', :port => 4567 unless TEST