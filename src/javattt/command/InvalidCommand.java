package javattt.command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/6/12
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidCommand implements Command {
    
    public void issue(Game game) {
        game.state.invalid();
    }
}
