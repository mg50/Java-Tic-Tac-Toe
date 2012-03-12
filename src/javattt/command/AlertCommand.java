package javattt.command;

import javattt.Game;
import javattt.fsm.PromptingAlertState;
import javattt.fsm.State;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/7/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlertCommand implements Command {
    public String message;


    public AlertCommand(String message) {
        this.message = message;
    }

    public Command sendToGame(Game game) {
        game.suspend();
        game.state = new PromptingAlertState(game, message);
        return new NullCommand();
    }
}
