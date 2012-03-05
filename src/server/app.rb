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


	def self.handshake(id)
		game = Connection[id].game
		game.start(nil) if game.stage.toString == "newGame"
	end

	get '/r/:room?' do
		unless session[:id]
			session[:id] = Connection.register(request.ip) 			
			Connection[session[:id]].game = ip_hash[@env['REMOTE_ADDR']] if ip_hash[@env['REMOTE_ADDR']]
		end
		Connection[session[:id]].game.room = params[:room]

		conn = Connection[session[:id]]
		if conn.game.stage == Java::Javattt.Stage::halt
			conn.game.stage = Java::Javattt.Stage::newGame
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
		App.handshake(session[:id]) unless conn.game.started?
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