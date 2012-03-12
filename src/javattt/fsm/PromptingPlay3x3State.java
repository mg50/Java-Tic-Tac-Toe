package javattt.fsm;

import javattt.command.Command;
import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class PromptingPlay3x3State extends State {
    public PromptingPlay3x3State(Game game) {
        super(game);
    }

    public Command execute() {
        game.state = new ReceivingPlay3x3State(game);
        return game.ui.promptPlay3x3();
    }
}