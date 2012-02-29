package javattt;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/22/12
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */


public class Game {
    public Stage stage = Stage.newGame;
    public UI ui;
    public Board board = new Board() {};
    public Player playerX;
    public Player playerO;
    public Player currentPlayer;
    public int xWinsCount;
    public int oWinsCount;

    
    public void move(int x, int y, Player player) {
        board.setCell(x, y, player.side);
    }

    
    public TransitionData transition() {
        return transition(null);
    }
    
    public TransitionData transition(TransitionData data) {
        TransitionData returnData = null;

        if(data != null) {
            if(data.signal == TransitionData.Signal.EXIT) stage = Stage.halt;
            else if(data.signal == TransitionData.Signal.PAUSE) return null;
            else if(data.signal == TransitionData.Signal.RESTART) {
                stage = Stage.newGame;
            }
        }
        

        switch(stage) {
            case newGame:
                playerX = null;
                playerO = null;
                stage = Stage.receivingPlay3x3;
                onNewGame();
                return ui.promptPlay3x3();

            case receivingPlay3x3:
                if(data.signal == TransitionData.Signal.INVALID) break;
                
                if(data.signal == TransitionData.Signal.YES) board = new Board(3);
                else board = new Board(4);
                
                stage = Stage.receivingPlayVsAI;
                return ui.promptPlayVsAI();

            case receivingPlayVsAI:
                if(data.signal == TransitionData.Signal.INVALID) { //If player enters invalid input
                    return ui.promptPlayVsAI();
                }
                else if(data.signal == TransitionData.Signal.YES) {
                    stage = Stage.promptingPlayAsX;
                }
                else {
                    playerX = currentPlayer = new HumanPlayer(Side.X);
                    playerO = new HumanPlayer(Side.O);
                    stage = Stage.queryingMove;
                    ui.update(board);
                }
                
                onReceivingPlayVsAI();
                break;

            case promptingPlayAsX:
                stage = Stage.receivingPlayAsX;
                return ui.promptPlayAsX();

            case receivingPlayAsX:
                if(data.signal == TransitionData.Signal.INVALID) {
                    stage = Stage.promptingPlayAsX;
                    break;
                }

                if(data.signal == TransitionData.Signal.YES) {
                    playerX = currentPlayer = new HumanPlayer(Side.X);
                    playerO = new AIPlayer(Side.O, board.size);
                }
                else {
                    playerX = currentPlayer = new AIPlayer(Side.X, board.size);
                    playerO = new HumanPlayer(Side.O);
                }

                onReceivingPlayAsX(data);
                stage = Stage.queryingMove;
                ui.update(board);

                break;

            case queryingMove:
                stage = Stage.receivingMove;
                return currentPlayer.determineNextMove(board, ui);
            
            case receivingMove:
                if(data.signal == TransitionData.Signal.INVALID) {
                    stage = Stage.queryingMove;
                    break;
                }
                int x = data.coords[0];
                int y = data.coords[1];
                if(board.getCell(x, y) == Side._) {
                    move(x, y, currentPlayer);
                    ui.update(board);
                    Side victor = board.winner();
                    if(victor != null || board.isDraw()) {
                        stage = Stage.gameOver;
                        if(victor != null) returnData = new TransitionData(victor);
                    }
                    else {
                        stage = Stage.queryingMove;
                        currentPlayer = currentPlayer == playerX ? playerO : playerX;
                    }

                    onSuccessfulMove(data);
                }
                else {
                    returnData = new TransitionData(TransitionData.Signal.INVALID);
                }

                break;

            case gameOver:
                Side victor;
                if(data != null) victor = data.side;
                else victor = null;
                if(victor == Side.X) xWinsCount++;
                else if(victor == Side.O) oWinsCount++;
                ui.victoryMessage(victor, xWinsCount, oWinsCount);
                
                stage = Stage.promptingStarNewGame;
                break;

            case promptingStarNewGame:
                stage = Stage.receivingStartNewGame;
                return ui.promptStartNewGame();

            case receivingStartNewGame:
                if(data.signal == TransitionData.Signal.INVALID) {
                    stage = Stage.promptingStarNewGame;
                }
                else if(data.signal == TransitionData.Signal.YES) {
                    stage = Stage.newGame;
                }
                else {
                    stage = Stage.halt;
                }
                break;

            case halt:
                onHalt();
                break;
        }

        return returnData;
    }

    public void start(TransitionData data) {
        while(stage != Stage.halt && (data == null || data.signal != TransitionData.Signal.PAUSE)) {
            data = transition(data);
        }
    }

    //hooks

    public void onNewGame() {};
    public void onSuccessfulMove(TransitionData data) {}
    public void onReceivingPlayVsAI() {}
    public void onHalt() {}
    public void onReceivingPlayAsX(TransitionData data) {}
}
