package javattt;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/22/12
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Game {
    public Stage stage = Stage.promptingPlayVsAi;
    public UI ui;
    public Board board = new Board() {};
    public void move(int x, int y, Player player) {};
    public void start() {};
    public Player playerX;
    public Player playerO;
    public Player currentPlayer;
    public Side winner;

    public void processStage() {
        if(stage == Stage.newGame) {
            if(ui.promptPlayVsAi()) {

            };
        }
        else if(stage == Stage.promptingPlayerSide) {
            ui.promptPlayVsAi();
        }
        else if(stage == Stage.waitingForMove) {

        }
        else {

        }
        while(stage != Stage.gameOver) {

        }
    }
    
    public Side progressGame() {

        if(stage == Stage.newGame) {
            if(ui.promptPlayVsAi()) {
                stage = Stage.promptingPlayerSide;
            }
            else stage = Stage.queryingMove;
        }
        else if(stage == Stage.queryingMove) {
            stage = Stage.receivedMove;
            currentPlayer.determineNextMove(board, ui);
        }
        else if(stage == Stage.receivedMove) {
            move(x, y, currentPlayer);
            winner = board.winner();

            if(winner != null || board.isDraw()) {
                stage = Stage.newGame;
                ui.victoryMessage(winner);
            }

            if(board.winner() == Side.X) {
                stage =  ;
            }
        }

        return winner;
    }
}
