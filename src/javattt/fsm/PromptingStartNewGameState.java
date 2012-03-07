package javattt.fsm;

import javattt.command.Command;
import javattt.Game;

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

    public Command execute() {
        game.state = new ReceivingStartNewGameState(game);
        return game.ui.promptStartNewGame();
    }
}
