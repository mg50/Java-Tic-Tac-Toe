package javattt.command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/6/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepCommand implements Command {
    
    public void issue(Game game) {
        game.state.step();
    }
}
