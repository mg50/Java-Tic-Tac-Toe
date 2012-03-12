package javattt.command;

import javattt.Game;
import javattt.fsm.WaitingState;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/12/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class WaitCommand implements Command {


    @Override
    public Command sendToGame(Game game) {
        game.state = new WaitingState(game);

        return new PauseCommand();
    }
}
