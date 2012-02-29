require File.join(File.dirname(__FILE__), '../javattt/ttt.jar')
require File.join(File.dirname(__FILE__), 'httpgame')
require File.join(File.dirname(__FILE__), 'HTTPUI')


class Connection
	@@id_counter = 0
	@@connections = {}

	attr_accessor :id, :game

	def disconnect
		@@connections.delete id
	end

	def self.register
		@@id_counter += 1
		@@connections[@@id_counter] = Connection.new @@id_counter
		@@id_counter
	end

	def initialize(id)
		@game = HTTPGame.new
	end

	def self.[](id)
		@@connections[id]
	end

	def self.clear_all
		@@id_counter = 0
		@@connections = {}
	end
end