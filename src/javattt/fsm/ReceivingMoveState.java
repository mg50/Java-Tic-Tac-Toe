package javattt.fsm;

import javattt.Game;
import javattt.Side;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivingMoveState extends State {
    public ReceivingMoveState(Game game) {
        super(game);
    }
    
    public TransitionData invalid() {
        game.state = new PromptingMoveState(game);
        return null;
    }

    public TransitionData move(int[] coords) {
        int x = coords[0];
        int y = coords[1];
        
        if(game.board.getCell(x, y) == Side._) {
            game.board.setCell(x, y, game.currentPlayer.side);
            game.ui.update(game.board);
            game.currentPlayer = game.currentPlayer == game.playerX ? game.playerO : game.playerX;
            
            Side winner = game.board.winner();
            if(winner == null && !game.board.isDraw()) {
                game.state = new PromptingMoveState(game);
                return null;
            }
            else {
                game.state = new GameOverState(game);
                TransitionData data = new TransitionData(TransitionData.Signal.VICTOR);
                data.side = winner;
                return data;
            }
        }
        else {
            return invalid();
        }
    }
}
