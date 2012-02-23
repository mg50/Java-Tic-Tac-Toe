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
    public void move(int x, int y, Player player) {};
    public Player playerX;
    public Player playerO;
    public Player currentPlayer;
    public int xWinsCount;
    public int oWinsCount;

    
    public TransitionData transition() {
        return transition(null);
    }
    
    public TransitionData transition(TransitionData data) {

        switch(stage) {
            case newGame:
                board = new Board();
                stage = Stage.receivingPlayVsAI;
                return new TransitionData(ui.promptPlayVsAi());

            case receivingPlayVsAI:
                if(data.bool == null) { //If player enters invalid input
                    stage = Stage.newGame;
                }
                else if(data.bool) {
                    stage = Stage.promptingPlayAsX;
                }
                else {
                    playerX = currentPlayer = new HumanPlayer(Side.X);
                    playerO = new HumanPlayer(Side.O);
                    stage = Stage.queryingMove;
                    ui.update(board);
                }
                break;

            case promptingPlayAsX:
                stage = Stage.receivingPlayAsX;
                return new TransitionData(ui.promptPlayAsX());

            case receivingPlayAsX:
                if(data.bool == null) {
                    stage = Stage.promptingPlayAsX;
                    break;
                }

                if(data.bool) {
                    playerX = currentPlayer = new HumanPlayer(Side.X);
                    playerO = new AIPlayer(Side.O);
                }
                else {
                    playerX = currentPlayer = new AIPlayer(Side.X);
                    playerO = new HumanPlayer(Side.O);
                }

                stage = Stage.queryingMove;
                ui.update(board);
                break;

            case queryingMove:
                stage = Stage.receivingMove;
                return new TransitionData(currentPlayer.determineNextMove(board, ui));
            
            case receivingMove:
                int x = data.move[0];
                int y = data.move[1];
                if(board.getCell(x, y) == Side._) {
                    move(x, y, currentPlayer);
                    ui.update(board);
                    Side victor = board.winner();
                    if(victor != null || board.isDraw()) {
                        stage = Stage.gameOver;
                        if(victor != null) return new TransitionData(victor);
                        else return null;
                    }
                    else {
                        stage = Stage.queryingMove;
                        currentPlayer = currentPlayer == playerX ? playerO : playerX;
                    }
                }
                else {
                    stage = Stage.queryingMove;
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
                return new TransitionData(ui.promptStartNewGame());

            case receivingStartNewGame:
                if(data == null) {
                    stage = Stage.promptingStarNewGame;
                }
                else if(data.bool) {
                    stage = Stage.newGame;
                }
                else {
                    stage = Stage.halt;
                }
            case halt:
                break;
        }

        return null;
    }

    public void start() {
        TransitionData result = null;
        while(stage != Stage.halt) {
            result = transition(result);
        }
    }
}
