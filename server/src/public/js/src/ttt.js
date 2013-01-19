var Board,
SIDES = {
  X: "X",
  O: "O",
  EMPTY: ""
};
var PAUSE;
var buttons;
var labels;
(function () {
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

  ALERT_SELECTOR = "#alert"
  ALERT_MESSAGE_SELECTOR = "#alert_message"

  var HOST = '/'


  Board = function (stage, gameState) {
    this.stage = stage;
    this.buildDom(gameState);
    this.lastTime = 0;

    this.gamePlaying = false;
    this.statusInterval = null;
  }

  Board.prototype.displayMessage = function (msg) {
    $(MESSAGE_SELECTOR).html(msg);
  }

  Board.prototype.buildDom = function (gameState) {
    var self = this;
    var size = gameState ? gameState.length : 3;
    var dom = $('<table id="board"></table>')

    var octagonTemplate = $('#octagon-template-container').children();

    for (var i = 0; i < size; i++) {
      var tr = $('<tr class="' + ROW_CLASS + '"/>')
      for (var j = 0; j < size; j++) {
        var td = $('<td class="' + CELL_CLASS + '".>');
        td.append(octagonTemplate.clone())
        if (gameState) td.find('.octagon-body').html(gameState[i][j]);
        tr.append(td)
      }
      dom.append(tr);
    }
    this.createListeners(dom);
    this.dom = dom.get(0);
    $(this.stage).append(dom);

    $("#restart a").click(function () {
      self.query("RESTART")

      return false;
    })

    $(window).unload(function () {
      self.query("EXIT")
    })


    function makeButton(id) {
      var button = $('#button').clone();
      button.attr('id', 'button' + id);
      $("#board").append(button);
    }

    function fillButton(num, txt) {
      labels.eq(num + 1).html(txt)
    }

    makeButton(1);
    makeButton(2);
    makeButton(3);
    buttons = $('.button');
    labels = buttons.find('.label')

    buttons.eq(1).find('a').click(function() {
      if(!buttonsEnabled) return;
      fadeAllOut(function() {
        self.query('YES')
      })
    })

    buttons.eq(2).find('a').click(function() {
      if(!buttonsEnabled) return;
      fadeAllOut(function() {
        self.query('NO')
      })
    })

    buttons = $("#button1, #button2, #button3")
    labels = buttons.find('.label')

    fadeAllIn();
  }



  var STEP = 50;
  var SPEED = 'medium'


  fadeAllOut = function (cb) {
    var x = 3;
    labels.animate({
      opacity: 0
    }, 'fast', function() {
      x--;
      if(x === 0) cb();
    })
  }

  fadeAllIn = function (cb) {
    labels.eq(0).animate({
      opacity: 1
    }, SPEED, function () {
      labels.eq(1).animate({
        opacity: 1
      }, SPEED, function () {
        labels.eq(2).animate({
          opacity: 1
        }, SPEED, cb)
      })
    })
  }

  $(document).ready(function() {
    alertBox = $('#alert')
    sendAlert = function(msg, cb) {
      alerting = true;
      alertBox.css({visibility: 'hidden'})
      alertBox.html(msg)

      width = alertBox.width();
      pageWidth = $('html').width();

      rightOfPage = -1*width;
      middleOfPage = pageWidth/2 + width/2;
      endOfPage = pageWidth + width


      alertBox.css({visibility: 'visible', right: (-1*width)})
      alertBox.animate({right: middleOfPage}, 2000, function() {
        alertBox.animate({right: endOfPage}, 500, function() {
          cb();
        })
      })
    }
  })

  var buttonsEnabled = true;
  disableButtons = function(cb) {
    if(!buttonsEnabled) return false;
    buttonsEnabled = false;
    buttons.find('a').attr('href', '').end().removeClass('enabled').addClass('disabled')
  }

  enableButtons = function(cb) {
    if(buttonsEnabled) return true;
    buttonsEnabled = true;
    buttons.find('a').attr('href', '#').end().addClass('enabled').removeClass('disabled')
  }










  Board.prototype.getCell = function (x, y) {
    return $(this.dom).find(ROW_SELECTOR).eq(y).find(CELL_SELECTOR).eq(x).html();
  }

  Board.prototype.remove = function () {
    //$(this.dom).find('*').unbind();
    $(this.dom).remove();
  }

  Board.prototype.createListeners = function (dom) {
    var self = this;
    $(dom).find(CELL_SELECTOR).click(function () {
      var row = $(this).closest(ROW_SELECTOR).get(0);
      var x = $(row).find(CELL_SELECTOR).index(this);
      var y = $(self.dom).find(ROW_SELECTOR).index(row);
      self.query("MOVE", {
        coords: [x, y]
      });
    })
  }

  Board.prototype.updateDom = function (gameState) {
    $(ROW_SELECTOR, this.dom).each(function (y) {
      $(CELL_SELECTOR, this).each(function (x) {
        $(this).find('.octagon-body').html(gameState[y][x])
      });
    })
  }

  Board.prototype.getStatus = function () {
    var self = this;
    if (!self.gamePlaying) return;

    this.statusInterval = setInterval(function () {
      if (PAUSE) return;
      $.ajax({
        url: HOST + 'status',
        type: 'post',
        dataType: 'json',
        success: function (r) {
          console.log("Server status: ", r);
          //if(board.lastTime <= r.timestamp) return;

          var currentBoardSize = $(ROW_SELECTOR).length;
          if (r.board.length !== currentBoardSize) {
            self.remove();
            self.buildDom(r.board);
          }
          self.updateDom(r.board);
          self.respondToServerStatus(r);
        },
        error: function (r) {
          console.log("Error querying status:", r.responseText)
        }
      });
    }, 500);
  }

  Board.prototype.query = function (signal, options) {
    options = options || {}

    $.ajax({
      url: HOST + 'query',
      type: 'post',
      data: 'signal=' + signal + '&options=' + JSON.stringify(options),
      dataType: 'json',
      success: function (r) {
        console.log("Response from query: ", r);
      },
      error: function (r) {
        console.log("Error from query:", r.responseText);
      }
    })
  }

  Board.prototype.confirm = function (msg, opt1, opt2) {
    if(labels.eq(0).html() === msg) return;
    labels.eq(0).html(msg)
    labels.eq(1).html(opt1)
    labels.eq(2).html(opt2)
    fadeAllIn();
  }

  Board.prototype.alert = function (msg, cb) {
    var alert = $(ALERT_SELECTOR);
    PAUSE = true;
    var self = this;
    sendAlert(msg, function() {
      self.query('ALERT_OK')
      PAUSE = false;
      cb()
    })
  }

  var firstMoveOfGame = false;
  Board.prototype.respondToServerStatus = function (msg) {
    var self = this;

    if (msg.ui.alert_message) {
      self.alert(msg.ui.alert_message);
    } else if (msg.ui.wait_message) {
      this.displayMessage(msg.ui.wait_message)
    } else if (msg.state === 'MoveState') {
      if(firstMoveOfGame) {
        firstMoveOfGame = false;
        self.alert("GO!!!!!!")
      }

      this.readyForMove = true;
      if (msg.currentPlayer == "you") this.displayMessage("It's your turn.")

      else this.displayMessage("It's player " + msg.currentPlayer + "'s turn.")
    } else if (msg.state === "Play3x3State") {
      self.confirm("choose arena size", "3x3", "4x4")
    } else if (msg.state === "PlayVsAIState") {
      firstMoveOfGame = true;
      self.confirm("play vs. computer?", "yes", "no")
    } else if (msg.state === "PlayAsXState") {
      self.confirm("choose your fighter", "x", "o")
    } else if (msg.state === "StartNewGameState") {
      self.confirm("play again?", "yes", "no")
    } else if (msg.state === "BeginningGameState") {
      self.displayMessage(msg.ui.wait_message);
    } else if (msg.state === 'HaltState') {
    } else console.log("Can't understand response: ", msg);
  }

  Board.prototype.begin = function () {
    var self = this;
    $.ajax({
      url: HOST,
      type: 'post',
      dataType: 'json',
      success: function (r) {
        console.log(r);
        self.gamePlaying = true;
        self.lastTime = r.timestamp;
        self.getStatus();


/*        self.alert('virtua ttt', function() {
          self.gamePlaying = true;
          self.lastTime = r.timestamp;
          self.getStatus();
          console.log(r);
        })*/
      },
      error: function (r) {
        //alert("Couldn't connect to server!");
        console.log(r.responseText)
      }
    })
  }
})();
