package javattt.command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/6/12
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Command {
    
    public void issue(Game game);
}
