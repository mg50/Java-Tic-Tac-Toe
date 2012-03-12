require File.join(File.dirname(__FILE__), 'httpgame')
include_class Java::Javattt::fsm.WaitingState

class Room

	@@rooms = {}

	attr_accessor :name, :owner, :player

	def self.room_of_game(game)
		@@rooms.each do |name, room|
			return room if room.owner == game or room.player == game
		end

		nil
	end

	def initialize(name)
		self.name = name
		@observers = []
		@@rooms[name] = self unless @@rooms[name]
	end

	def remove_game(game)
		if game == self.owner
			self.owner = self.player
			self.player = nil
		elsif game == self.player
			self.player = @observers[0]
			@observers.shift unlesss @observers.empty?
		elsif @observers.include? game
			@observers.delete game
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
		else
			game.duplicate_game_state owner

			if self.player.nil?
				self.player = game
			else
				@observers << game
			end
		end

		self.owner.opponent_game = self.player if self.owner
		self.player.opponent_game = self.owner if self.player

		game.room = self

		suspend_games_until_owner_starts unless game == self.owner or self.owner.playing		
	end

	def suspend_games_until_owner_starts
		games = self.player ? [self.player] + @observers : @observers
		games.each do |game|
			unless game.state.class == WaitingState
				game.receive_signal "signal" => "WAIT", 
									"message" => "Waiting for room owner to begin two-player game."
			end
		end
	end

	def full?
		self.owner and self.player
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