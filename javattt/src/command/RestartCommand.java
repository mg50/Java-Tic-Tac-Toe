package javattt.command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/9/12
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class RestartCommand implements Command{
    
    public void issue(Game game) {
        game.state.restart();
    }
}
