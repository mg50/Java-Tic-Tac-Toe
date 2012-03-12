require File.join(File.dirname(__FILE__), 'httpgame')

class Room

	@@rooms = {}

	attr_accessor :name, :owner, :player

	def initialize(name)
		self.name = name
		@@rooms[name] = self unless @@rooms[name]
	end

	def remove_game(game)
		if game == self.owner
			self.owner = self.player
			self.player = nil
		elsif game == self.player
			self.player = nil
		end

		game.room = nil
		game.receive_signal "signal" => "RESTART"

		if self.owner.nil? and self.player.nil?
			@@rooms.delete @name
		end
	end

	def add_game game
		if self.owner.nil?
			self.owner = game
		elsif self.player.nil?
			self.player = game
		end

		game.room = self
	end

	def self.exists? name
		not @@room[name].nil?
	end

	def self.[](name)
		@@rooms[name] || Room.new(name)
	end

	def self.[]=(name, val)
		@@rooms[name] = val
	end

	def self.create_empty_room
		room_name = nil
		while room_name.nil? or @@rooms[room_name]
			room_name = rand(1000000).to_s
		end

		Room[room_name]
	end

	def self.destroy_all_rooms
		@@rooms = {}
	end
end