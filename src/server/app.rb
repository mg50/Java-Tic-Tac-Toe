require 'rubygems'
require 'sinatra'
require 'json'
require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'connection')
require File.join(File.dirname(__FILE__), 'solver')
require File.join(File.dirname(__FILE__), 'HTTPUI')

TEST = false unless defined? TEST

class App < Sinatra::Base
	def self.handshake(id)
		Connection[id].game.start nil
		resp = {"id" => id}
	end

	def self.get_status(id)
		game = Connection[id].game
		resp = {"stage" => game.stage.toString,
				"board" => game.board}
	end

	def self.query(id, params)
		game = Connection[id].game
		data = game.interpret params
		#binding.pry
		game.start(data)
	end

	get '/' do
		session[:id] = Connection.register unless session[:id]
	end

	post '/' do
		App.handshake 1
	end

	post '/query' do
		id = blah
		App.query id, JSON.parse(params)
	end
end

App.run! :host => 'localhost', :port => 80 unless TEST