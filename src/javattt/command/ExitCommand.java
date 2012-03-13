package javattt.command;

import javattt.Game;
import javattt.fsm.HaltState;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/7/12
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExitCommand implements Command {
    
    public void issue(Game game) {
        game.state = new HaltState(game);
    }
}
