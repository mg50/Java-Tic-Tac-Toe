package javattt.fsm;

import javattt.command.Command;
import javattt.Game;
import javattt.Side;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class MoveState extends State {
    public MoveState(Game game) {
        super(game);
    }


    public Command readNextCommand() {
        return game.currentPlayer.gameStrategy.determineNextMove(game.currentPlayer.side, game.board,
            game.currentPlayer.ui);
    }


    public void move(int[] coords) {
        int x = coords[0];
        int y = coords[1];
        if(game.board.getCell(x, y) == Side._) {
            game.board.setCell(x, y, game.currentPlayer.side);
            game.playerX.ui.update(game.board);
            game.playerO.ui.update(game.board);

            game.currentPlayer = game.currentPlayer == game.playerX ? game.playerO : game.playerX;
            
            Side winner = game.board.winner();
            if(winner != null || game.board.isDraw()) {
                game.state = new GameOverState(game);
            }

            game.onSuccessfulMove(coords);
        }
    }
}
