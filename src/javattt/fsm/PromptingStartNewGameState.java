package javattt.fsm;

import javattt.Game;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class PromptingStartNewGameState extends State {
    public PromptingStartNewGameState(Game game) {
        super(game);
    }

    public TransitionData pass() {
        game.state = new ReceivingStartNewGameState(game);
        return game.ui.promptStartNewGame();
    }
}
