require 'rubygems'
require 'sinatra'
require 'json'
require File.join(File.dirname(__FILE__), 'solver')

TEST = false unless defined? TEST

class App < Sinatra::Base
	@@SEARCHING_FOR_GAME = []

	get '/' do
		conn = Connection.new
		@@waiting << game

		File.read(File.join('public/html', 'index.html'))		
	end

	post '/query' do
		conn = Connection.new
		signal = HTTPGame.interpret params
		raise unless signal

		Game[conn].start signal		
	end

	get '/test' do
		File.read(File.join('public/html', 'test.html'))
	end


	post '/status' do
		conn = Connection.new
		Game[conn].ui.status
	end

	post '/' do

		game = Coordinator.game_of id
		App.respond_to_ttt_request(params[:gameState], params[:numPlayers])
	end

	def self.respond_to_ttt_request(game_string, num_players)
		begin
			game = JSON.parse game_string
			num_players = num_players.to_i
			side = game.flatten.select{|x| x == ""}.length.even? ? "O" : "X"
			solver_response = Solver.new(num_players).solve(game, side)
		rescue
			bad_json = true
		end

		response = {}
		if not bad_json and solver_response
			response['success'] = true
			response['move'] = solver_response['move']
			response['outcome'] = solver_response['outcome']
		else
			response['success'] = false
		end

		JSON.generate response		
	end
end

App.run! :host => 'localhost', :port => 3000 unless TEST