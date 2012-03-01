require 'rubygems'
require 'sinatra'
require 'json'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'connection')
require File.join(File.dirname(__FILE__), 'solver')
require File.join(File.dirname(__FILE__), 'HTTPUI')

TEST = false unless defined? TEST

class App < Sinatra::Base
	enable :sessions

	def self.handshake(id)
		game = Connection[id].game
		game.start(nil) if game.stage.toString == "newGame"
	end

	get '/r/:room?' do
		session[:id] = Connection.register unless session[:id]
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
		JSON.generate "response" => true
	end

	get '/test' do
		File.read(File.join('public/html', 'test.html'))
	end
end

App.run! :host => 'localhost', :port => 3000 unless TEST