var Board, 
	SIDES = {X: "X", O: "O", EMPTY: ""};

(function() {
	var X = SIDES.X,
		O = SIDES.O,
		EMPTY = SIDES.EMPTY;

	var ROW_CLASS = 'row',
		CELL_CLASS = 'cell',
		ROW_SELECTOR = '.' + ROW_CLASS,
		CELL_SELECTOR = '.' + CELL_CLASS;



	Board = function(stage, gameState) {
		this.dom = Board.buildDom(gameState);
		$(stage).append(this.dom);

		this.currentPlayer = X;
		this.readyForMove = true;
		this.playVsAi = false;

		this.createListeners();
	}

	Board.buildDom = function(gameState) {
		var dom = $('<table id="board"></table>')
		for(var i = 0; i < 3; i++) {
			var tr = $('<tr class="' + ROW_CLASS + '"/>')
			for(var j = 0; j < 3; j++) {
				var td = $('<td class="' + CELL_CLASS + '" />');
				if(gameState) td.html(gameState[i][j]);
				tr.append(td)
			}
			dom.append(tr);
		}

		return dom.get(0);
	}

	Board.prototype.getCell = function(x, y) {
		return $(this.dom).find(ROW_SELECTOR).eq(y).find(CELL_SELECTOR).eq(x).html();
	}

	Board.prototype.getCellValue = function(x, y) {
		return this.getCell.html();
	}

	Board.prototype.remove = function() {
		$(this.dom).find('*').unbind();
		$(this.dom).remove();
	}

	Board.prototype.createListeners = function() {
		var self = this;
		$(this.dom).find(CELL_SELECTOR).click(function() {
			var tr = $(this).closest(ROW_SELECTOR).get(0);
			var x = $(tr).find(CELL_SELECTOR).index(this);
			var y = $(self.dom).find(ROW_SELECTOR).index(tr);
			self.move(x, y, function() {
				self.sendBoardState();
			});
		})
	}

	Board.prototype.jsonify = function() {
		var gameState = [];
		$(this.dom).find(ROW_SELECTOR).each(function() {
			var row = [];
			$(this).find(CELL_SELECTOR).each(function() {
				row.push($(this).html());
			})
			gameState.push(row);
		})

		return JSON.stringify(gameState);
	}

	Board.prototype.begin = function() {
		//Clear board		
		$(this.dom).find(CELL_SELECTOR).each(function() {
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
		var cell = $(this.dom).find(ROW_SELECTOR).eq(y).find(CELL_SELECTOR).eq(x)
		if(!this.readyForMove || cell.html() !== EMPTY) return;

		cell.html(this.currentPlayer);
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
})();