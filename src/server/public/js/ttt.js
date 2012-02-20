(function() {
	var X = "X";
	var O = "O";
	var EMPTY = "";

	function Board(selector) {
		this.dom = $(selector).get(0);
		this.currentPlayer = X;
		this.readyForMove = true;
		this.playVsAi = false;

		this.createListeners();
	}

	Board.prototype.createListeners = function() {
		var self = this;
		$(this.dom).find('td').click(function() {
			var tr = $(this).parent().get(0);
			var x = $(tr).find('td').index(this);
			var y = $(self.dom).find('tr').index(tr);
			self.move(x, y, function() {
				self.sendBoardState();
			});
		})
	}

	Board.prototype.jsonify = function() {
		var gameState = [];
		$(this.dom).find('tr').each(function() {
			var row = [];
			$(this).find('td').each(function() {
				row.push($(this).html());
			})
			gameState.push(row);
		})

		return JSON.stringify(gameState);
	}

	Board.prototype.begin = function() {
		//Clear board		
		$(this.dom).find('td').each(function() {
			$(this).html(EMPTY);
		});

		this.currentPlayer = X;
		if(confirm("Play vs. AI?")) {
			this.playVsAi = true;

			if(!confirm("Play as X?")) {
				this.sendBoardState();
			}
		}
		else {
			this.playVsAi = false;
		}
	}

	Board.prototype.move = function(x, y, callback) {
		var td = $(this.dom).find('tr').eq(y).find('td').eq(x)
		if(!this.readyForMove || td.html() !== EMPTY) return;

		td.html(this.currentPlayer);
		this.currentPlayer = this.currentPlayer === X ? O : X;

		if(typeof callback === "function") callback();
	}

	Board.prototype.sendBoardState = function() {
		var self = this;
		self.readyForMove = false;
		$.ajax({
			url: 'http://localhost:3000/',
			type: 'post',
			dataType: 'json',
			data: 'gameState=' + self.jsonify() + '&numPlayers=' + (self.playVsAi ? 1 : 2),
			success: function(response) {
				self.readyForMove = true;

				if(response.move && self.playVsAi) {
					self.move(response.move[0], response.move[1]);
				}
				if(response.outcome) {
					alert(response.outcome);
					if(confirm('Play again?')) {
						self.begin();
					}
					else self.readyForMove = false;
				}
			},
			error: function(e) {
				console.log(e);
				alert("Error!");
			}
		});
	}

	$(document).ready(function() {
		var board = new Board('#board');

		board.begin();
	})
})();