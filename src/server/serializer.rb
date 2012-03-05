require File.join(File.dirname(__FILE__), 'httpgame')

class Serializer
	def game_hash(game)
		hash = {}
		hash[:state] = game.state.class.name.split("::").last
		hash[:waiting_for_opponent] = game.waiting_for_opponent		
		hash[:playerX] = game.playerX ? game.playerX.class.name.split("::").last : nil
		hash[:playerO] = game.playerO ? game.playerO.class.name.split("::").last : nil
		hash[:ip] = game.ip if game.ip

		if not game.currentPlayer
			hash[:currentPlayer] = nil
		elsif game.currentPlayer == game.playerX
			hash[:currentPlayer] = "X"
		else game.currentPlayer == game.playerO
			hash[:currentPlayer] = "O"
		end

		hash[:board] = game.get_ruby_grid
		hash[:room] = game.room

		hash
	end

	def board_from_ruby_grid(rows)
		java_rows = rows.map do |row|
			java_row = row.map {|side| java_side side}
		end.to_java(Java::Javattt.Side[])		
	end

	def revive(hash)
		game = HTTPGame.new

		state_string = "Java::Javattt::fsm." + hash[:state]
		game.state = eval(state_string).new game
 	
		game.room = hash[:room]
		game.waiting_for_opponent = hash[:waiting_for_opponent]
		game.ip = hash[:ip] if hash[:ip]

		grid = board_from_ruby_grid hash[:board]
		game.board = Java::Javattt.Board.new grid

		game.playerX = make_player("X", hash[:playerX], game.board)
		game.playerO = make_player("O", hash[:playerO], game.board)

		if hash[:currentPlayer] == "X"
			game.currentPlayer = game.playerX
		elsif hash[:currentPlayer] == "O"
			game.currentPlayer = game.playerO
		else
			game.currentPlayer = nil
		end

		game
	end

	def make_player(side, player_class, board)
		java_side = side == "X" ? Java::Javattt.Side::X : Java::Javattt.Side::O
		if player_class == "HumanPlayer"
			Java::Javattt.HumanPlayer.new java_side
		elsif player_class == "AIPlayer"
			Java::Javattt.AIPlayer.new java_side, board.size
		else
			nil
		end
	end

	def java_side side
		if side == "X"
			Java::Javattt.Side::X
		elsif side == "O"
			Java::Javattt.Side::O
		else
			Java::Javattt.Side::_
		end
	end

	def serialize_and_save(games=nil)
		if games.nil?
			games = Connection[].values.map {|c| c.game}
		end
		File.open(File.join(File.dirname(__FILE__), 'serialization.txt'), 'w') do |file|			
			file.write(Marshal.dump games.map {|game| game_hash game})
		end
	end

	def deserialize_all
		File.open(File.join(File.dirname(__FILE__), 'serialization.txt'), 'r') do |file|
			game_hashes = Marshal.load(file.read)
			game_hashes.map {|hash| revive hash}
		end
	end
end