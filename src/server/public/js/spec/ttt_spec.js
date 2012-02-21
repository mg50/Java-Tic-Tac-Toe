
$(document).ready(function() {
	var X = SIDES.X,
		O = SIDES.O,
		EMPTY = SIDES.EMPTY

	var stage = "#board_wrapper"

	describe("Board", function() {
		var board, board2;

		afterEach(function() {
			if(board) board.remove();
			if(board2) board2.remove();
		})

		it("initializes properly", function() {
			var game = [[X, X, O], [X, O, EMPTY], [EMPTY, EMPTY, O]];

			board = new Board(stage, game);
			for(var i = 0; i < 3; i++) {
				for(var j = 0; j < 3; j++) {
					var side = $(stage).find('tr').eq(i).find('td').eq(j).html();
					expect(side).toEqual(game[i][j])
				}
			}
		});

		it("it gets the value of a cell", function() {
			var game = [[X, X, O], [X, O, EMPTY], [EMPTY, EMPTY, O]];

			board = new Board(stage, game);
			for(var i = 0; i < 3; i++) {
				for(var j = 0; j < 3; j++) {
					var side = board.getCell(j, i);
					expect(side).toEqual(game[i][j])
				}
			}
		});		

		it("encodes itself as JSON correctly", function() {
			var game = [[X, X, O], [X, O, EMPTY], [EMPTY, EMPTY, O]];
			var board = new Board(stage, game);
			expect(board.jsonify()).toEqual('[["X","X","O"],["X","O",""],["","","O"]]');

			var game2 = [[EMPTY, EMPTY, O], [O, X, X], [X, X, O]];
			var board2 = new Board(stage, game2);
			expect(board2.jsonify()).toEqual('[["","","O"],["O","X","X"],["X","X","O"]]');			
		})

		it("moves as X", function() {
			var game = [[X, X, O], [X, O, EMPTY], [EMPTY, EMPTY, O]];
			var board = new Board(stage, game);
			board.currentPlayer = X;
						
			board.move(2, 1);
			var side = board.getCell(2, 1);
			expect(side).toEqual(X);
		})

		it("moves as O", function() {
			var game = [[X, X, O], [X, O, EMPTY], [EMPTY, EMPTY, O]];
			var board = new Board(stage, game);
			board.currentPlayer = O;
						
			board.move(0, 2);
			var side = board.getCell(0, 2);
			expect(side).toEqual(O);
		})

		it("doesn't move on a non-empty square", function() {
			var game = [[X, X, O], [X, O, EMPTY], [EMPTY, EMPTY, O]];
			var board = new Board(stage, game);
			board.currentPlayer = X;
						
			board.move(2, 0);
			var side = board.getCell(2, 0);
			expect(side).toEqual(O);
		})
	})
});