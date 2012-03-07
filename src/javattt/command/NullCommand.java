package javattt.command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/6/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NullCommand implements Command {
    
    public Command sendToGame(Game game) {
        return game.state.execute();
    }
}
