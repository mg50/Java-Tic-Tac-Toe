require File.join(File.dirname(__FILE__), 'httpgame')

class Serializer
	def player_hash(player)
		{:side => player.side.toS, :game_strategy => player.gameStrategy.class, :ip => player.ip,
			:class => player.class.split("::").last}
	end

	def game_hash(game)
		hash = {}
		hash[:masterPlayer] = player_hash game.masterPlayer if game.masterPlayer
		hash[:client_player] = player_hash game.client_player if game.client_player
		hash[:playerX] = player_hash game.playerX if game.playerX
		hash[:playerO] = player_hash game.playerO if game.playerO
		hash[:state] = game.state.class.name.split("::").last
		hash[:board] = game.get_ruby_grid
		hash[:room] = game.room

		if game.playerX == game.masterPlayer
			hash[:master_player_is_X] = true
		elsif game.playerO == game.masterPlayer
			hash[:master_player_is_O] = true
		end

		if game.client_player == game.playerX
			hash[:clientPlayerIsX] = true
		elsif game.client_player == game.playerO
			hash[:client_player_is_O] = true
		end

		if game.currentPlayer == game.playerX
			hash[:current_player_is_X] = true
		elsif game.currentPlayer == game.playerO
			hash[:current_player_is_O] = true
		end

		hash
	end

	def board_from_ruby_grid(rows)
		java_rows = rows.map do |row|
			java_row = row.map {|side| java_side side}
		end.to_java(Java::Javattt.Side[])		
	end


	def make_game(hash)
		game = HTTPGame[hash[:room]]
		game.board = board_from_ruby_grid hash[:board]
		game.state = eval("Java::Javattt::state." + hash[:state]).new game

		game.masterPlayer = make_player hash[:masterPlayer]
		game.client_player = make_player hash[:client_player]
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

	def make_player(ip, side, player_class, player_strategy_class, board)
		if player_class == "HTTPPlayer"
			player = HTTPPlayer.new
		else
			player = Java::Javattt.Player.new
		end

		if side
			java_side = side == "X" ? Java::Javattt.Side::X : Java::Javattt.Side::O
		end

		player.side = side

		if player_strategy_class == "HumanStrategy"
			Java::Javattt.HumanStrategy.new 
		elsif player_strategy_class == "AIStrategy"
			Java::Javattt.AIStrategy.new board.size
		end

		player.ip = ip

		player
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