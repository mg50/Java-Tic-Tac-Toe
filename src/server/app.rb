require 'rubygems'
require 'sinatra'
require 'json'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'connection')
require File.join(File.dirname(__FILE__), 'httpui')
require File.join(File.dirname(__FILE__), 'serializer')

TEST = false unless defined? TEST

class App < Sinatra::Base
	enable :sessions

	all_previous_games = Serializer.new.deserialize_all || []
	ip_hash = {}
	all_previous_games.each do |game|
		ip_hash[game.ip] = game
	end

	def self.start_game
	end

	def self.handshake(id)
		game = Connection[id].game
		game.start(nil) if game.state.class == Java::Javattt::fsm.NewGameState
	end

	get '/r/:room?' do
		unless session[:id]
			session[:id] = Connection.register(request.ip) 			
			if saved_game = ip_hash[@env['REMOTE_ADDR']] and saved_game.room == params[:room]
				Connection[session[:id]].game = saved_game
			end
		end

		game = Connection[session[:id]].game

		if game.room != params[:room]
			game.room = params[:room]
			game.restart
		end

		if game.state.class == Java::Javattt::fsm.HaltState
			game.restart
		end		

		File.read(File.join('public/html', 'index.html'))
	end

	post '/' do
		conn = Connection[session[:id]]
		App.handshake(session[:id])
		JSON.generate({"id" => session[:id], "timestamp" => Time.now.to_i})
	end

	post '/status' do
		conn = Connection[session[:id]]
		App.handshake(session[:id])
		resp = conn.game.status

		JSON.generate resp
	end

	post '/query' do
		id = session[:id]
		Connection[id].game.receive_signal JSON.parse params['json']
		Serializer.new.serialize_and_save
		JSON.generate "response" => true
	end

	get '/test' do
		File.read(File.join('public/html', 'test.html'))
	end
end

App.run! :host => 'localhost', :port => 3000 unless TEST