require 'rubygems'
require 'sinatra'
require 'json'
require 'pry'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'httpui')
require File.join(File.dirname(__FILE__), 'serializer')
require File.join(File.dirname(__FILE__), 'room')
include_class Java::Javattt::command.RestartCommand
include_class Java::Javattt::fsm.HaltState
TEST = false unless defined? TEST

class App < Sinatra::Base
	enable :sessions

	all_previous_games = Serializer.new.deserialize_all || []
	ip_hash = {}
	all_previous_games.each do |game|
		#ip_hash[game.ip] = game
	end

	get '/' do
		redirect '/r/'
	end

	get '/r/:room' do
#		unless session[:id]
#			session[:id] = Connection.register(request.ip) 			
#			if saved_game = ip_hash[@env['REMOTE_ADDR']] and saved_game.room == params[:room]
#				Connection[session[:id]].game = saved_game
#			end
#		end

#		game = Connection[session[:id]].game

		player = HTTPPlayer[session]
		game = HTTPGame[params[:room]]

		game = HTTPGame[session]
		current_room = Room[params[:room]]
		prior_room_of_game = Room.room_of_game game

		if prior_room_of_game != current_room
			prior_room_of_game.remove_game game if prior_room_of_game
			current_room.add_game game
		end

		if game.state.is_a? HaltState
			game.receive_signal "signal" => "RESTART"
		end		

		File.read(File.join('public/html', 'index.html'))
	end

	post '/' do
		JSON.generate "id" => session[:session_id], "timestamp" => Time.now.to_i
	end

	post '/status' do
		JSON.generate HTTPGame[session].status
	end

	post '/query' do
		HTTPGame[session].receive_signal JSON.parse(params['json'])
		#Serializer.new.serialize_and_save
		JSON.generate "response" => true
	end

	get '/test' do
		File.read(File.join('public/html', 'test.html'))
	end
end

App.run! :host => 'localhost', :port => 4567 unless TEST