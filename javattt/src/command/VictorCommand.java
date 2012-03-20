package javattt.command;

import javattt.Game;
import javattt.Side;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/7/12
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class VictorCommand implements Command {
    public Side side;
    
    public VictorCommand(Side s) {
        side = s;
    }
    
    public void issue(Game game) {
        game.state.victor(side);
    }
}
