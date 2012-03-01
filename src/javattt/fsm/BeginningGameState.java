package javattt.fsm;

import javattt.Game;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeginningGameState extends State {
    public BeginningGameState(Game game) {
        super(game);
    }
    
    public TransitionData pass() {
        game.currentPlayer = game.playerX;
        game.ui.update(game.board);
        game.state = new PromptingMoveState(game);
        return null;
    }
}
