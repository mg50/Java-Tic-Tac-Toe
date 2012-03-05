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

	def self.register(ip=nil)
		@@id_counter += 1
		@@connections[@@id_counter] = Connection.new @@id_counter, ip
		@@id_counter
 	end

	def initialize(id, ip)
		@game = HTTPGame.new
		@game.ip = ip
	end

	def self.[](id=nil)
		if id.nil?
			@@connections
		else
			@@connections[id]
		end
	end

	def self.clear_all
		@@id_counter = 0
		@@connections = {}
	end
end