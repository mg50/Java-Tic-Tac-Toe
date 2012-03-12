package javattt.fsm;

import javattt.Game;
import javattt.command.Command;
import javattt.command.InvalidCommand;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/7/12
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivingAlertState extends State {

    public ReceivingAlertState(Game game) {
        super(game);
    }

    public Command execute() {
        game.unsuspend();
        return new InvalidCommand();
    }
}
