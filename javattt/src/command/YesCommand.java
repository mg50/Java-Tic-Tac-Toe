package javattt.command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/6/12
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class YesCommand implements Command {
    
    public void issue(Game game) {
        game.state.yes();
    }
}
