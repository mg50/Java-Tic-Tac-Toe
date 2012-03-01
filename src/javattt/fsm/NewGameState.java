package javattt.fsm;

import javattt.Game;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameState extends State {
    
    public NewGameState(Game game) {
        super(game);
    }

    public TransitionData pass() {
        game.playerX = null;
        game.playerO = null;
        game.currentPlayer = null;
        game.board = null;
        game.onNewGame();
        game.state = new PromptingPlay3x3State(game);
        return null;
    }
}
