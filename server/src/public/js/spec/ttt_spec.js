$(document).ready(function() {
	var X = SIDES.X,
		O = SIDES.O,
		_ = SIDES.EMPTY

	var stage = "#board_wrapper"

	describe("Board", function() {
		var board, board2;

		afterEach(function() {
			if(board) board.remove();
			if(board2) board2.remove();
		})

		it("initializes properly with a 3x3 board", function() {
			var game = [[X, X, O], [X, O, _], [_, _, O]];

			board = new Board(stage, game);
			for(var i = 0; i < 3; i++) {
				for(var j = 0; j < 3; j++) {
					var side = $(stage).find('tr').eq(i).find('td').eq(j).html();
					expect(side).toEqual(game[i][j])
				}
			}
		});

		it("initializes properly with a 4x4 board", function() {
			var game = [[X, X, O, _], [X, O, _, X], [_, _, O, O], [X, X, _, _]];

			board = new Board(stage, game);
			for(var i = 0; i < 4; i++) {
				for(var j = 0; j < 4; j++) {
					var side = $(stage).find('tr').eq(i).find('td').eq(j).html();
					expect(side).toEqual(game[i][j])
				}
			}
		});		

		it("it gets the value of a cell", function() {
			var game = [[X, X, O], [X, O, _], [_, _, O]];

			board = new Board(stage, game);
			for(var i = 0; i < 3; i++) {
				for(var j = 0; j < 3; j++) {
					var side = board.getCell(j, i);
					expect(side).toEqual(game[i][j])
				}
			}
		});		

		it("updates the game", function() {
			board = new Board(stage);

			var gameState = [[X, O, X], [O, _, _], [_, _, X]]
			board.updateDom(gameState);

			expect(board.getCell(0, 0)).toEqual(X);
			expect(board.getCell(1, 0)).toEqual(O);
			expect(board.getCell(1, 1)).toEqual(_);
			expect(board.getCell(2, 2)).toEqual(X);
		})
	});
});