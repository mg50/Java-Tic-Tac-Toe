package javattt.fsm;

import javattt.Game;
import javattt.command.Command;
import javattt.command.NullCommand;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/7/12
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PromptingAlertState extends State {
    String string;
    
    public PromptingAlertState(Game game, String s) {
        super(game);
        string = s;
    }

    public Command execute() {
        game.state = new ReceivingAlertState(game);
        return game.ui.displayMessage(string);
    }
}
