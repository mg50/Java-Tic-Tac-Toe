package command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/6/12
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoCommand implements Command {
    
    public Command sendToGame(Game game) {
        return game.state.no();
    }
}
