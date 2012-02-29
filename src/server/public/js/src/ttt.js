var Board, 
	SIDES = {X: "X", O: "O", EMPTY: ""};
var PAUSE;
(function() {
	var X = SIDES.X,
		O = SIDES.O,
		EMPTY = SIDES.EMPTY;

	var ROW_CLASS = 'row',
		CELL_CLASS = 'cell',
		ROW_SELECTOR = '.' + ROW_CLASS,
		CELL_SELECTOR = '.' + CELL_CLASS,
		MESSAGE_SELECTOR = '#message'
		CONFIRM_SELECTOR = '#confirm'
		QUESTION_SELECTOR = "#question"

	var HOST = 'http://localhost:3000/'


	Board = function(stage, gameState) {
		this.stage = stage;
		this.buildDom(gameState);
		this.lastTime = 0;

		this.gamePlaying = false;
		this.statusInterval = null;
	}

	Board.prototype.displayMessage = function(msg) {
		$(MESSAGE_SELECTOR).html(msg);
	}

	Board.prototype.buildDom = function(gameState) {
		var self = this;
		var size = gameState ? gameState.length : 3;
		var dom = $('<table id="board"></table>')
		for(var i = 0; i < size; i++) {
			var tr = $('<tr class="' + ROW_CLASS + '"/>')
			for(var j = 0; j < size; j++) {
				var td = $('<td class="' + CELL_CLASS + '" />');
				if(gameState) td.html(gameState[i][j]);
				tr.append(td)
			}
			dom.append(tr);
		}
		this.createListeners(dom);
		this.dom = dom.get(0);
		$(this.stage).append(dom);

		$(CONFIRM_SELECTOR).find("*").unbind().end()
		.dialog({
			modal: true,
			autoOpen: false
		})
		.find('a').each(function(idx) {
			var signal;
			if(idx === 0) signal = "YES";
			else signal = "NO";

			$(this).click(function() {
				self.query({signal: signal});
				$(CONFIRM_SELECTOR).dialog('close');
			})
		})
	}

	Board.prototype.getCell = function(x, y) {
		return $(this.dom).find(ROW_SELECTOR).eq(y).find(CELL_SELECTOR).eq(x).html();
	}

	Board.prototype.remove = function() {
		$(this.dom).find('*').unbind();
		$(this.dom).remove();
	}

	Board.prototype.createListeners = function(dom) {
		var self = this;
		$(dom).find(CELL_SELECTOR).click(function() {
			var row = $(this).closest(ROW_SELECTOR).get(0);
			var x = $(row).find(CELL_SELECTOR).index(this);
			var y = $(self.dom).find(ROW_SELECTOR).index(row);
			self.query({
				signal: "MOVE",
				coords: [x, y]
			})
		})
	}

	Board.prototype.updateDom = function(gameState) {
		$(ROW_SELECTOR, this.dom).each(function(y) {
			$(CELL_SELECTOR, this).each(function(x) {
				$(this).html(gameState[y][x])
			});
		})
	}

	Board.prototype.getStatus = function() {
		var self = this;
		if(!self.gamePlaying) return;

		this.statusInterval = setInterval(function() {
			if(PAUSE) return;
			$.ajax({
				url: HOST + 'status',
				type: 'post',
				dataType: 'json',
				success: function(r) {
					console.log("Server status: ", r);
					//if(board.lastTime <= r.timestamp) return;

					var currentBoardSize = $(ROW_SELECTOR).length;
					if(r.board.length !== currentBoardSize) {
						self.remove();
						self.buildDom(r.board);
					}
					self.updateDom(r.board);
					self.respondToServerStatus(r);
				}
			});
		}, 500);
	}

	Board.prototype.query = function(params, cb) {
		$.ajax({
			url: HOST + 'query',
			type: 'post',
			data: 'json=' + JSON.stringify(params),
			dataType: 'json',
			success: function(r) {
				console.log("Response from query: ", r);
				if(typeof cb === 'function') cb();
			}
		})
	}

	Board.prototype.confirm = function(msg, opt1, opt2) {
		var confirm = $(CONFIRM_SELECTOR);
		confirm.dialog('open');
		confirm.find(QUESTION_SELECTOR).html(msg);
		confirm.find('a').each(function(idx) {
			if(idx === 0) $(this).html(opt1);
			else $(this).html(opt2);
		})
	}

	Board.prototype.respondToServerStatus = function(msg) {
		var self = this;
		if(msg.stage === 'receivingMove') {
			this.readyForMove = true;
			this.displayMessage("It's player " + msg.currentPlayer + "'s turn.")
		}

		else if(msg.stage === "receivingPlay3x3") {
			self.confirm("What size board would you like to play on?", "3x3", "4x4")
		}

		else if(msg.stage === "receivingPlayVsAI") {
			this.query({
				signal: confirm("Play vs. AI?") ? "YES" : "NO"
			});
		}


		else if(msg.stage === "receivingPlayAsX") {
			this.query({
				signal: confirm('Play as X?') ? "YES" : "NO"
			});
		}

		else if(msg.stage === "receivingStartNewGame") {
			this.query({
				signal: confirm("Start new game?") ? "YES" : "NO"
			});
		}

		else if(msg.stage === 'halt') {
			this.gamePlaying = false;
			this.displayMessage("");
			clearInterval(this.statusInterval);
		}


		else console.log("Can't understand response: ", msg);
	}

	Board.prototype.begin = function() {
		var self = this;
		$.ajax({
			url: HOST,
			type: 'post',
			dataType: 'json',
			success: function(r) {
				console.log(r);
				self.gamePlaying = true;
				self.lastTime = r.timestamp;
				self.getStatus();
				console.log(r);
			},
			error: function(r) {
				//alert("Couldn't connect to server!");
				console.log(r.responseText)
			}
		})		
	}
})();