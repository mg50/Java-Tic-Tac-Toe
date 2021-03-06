package javattt.command;

import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/6/12
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoveCommand implements Command {
    
    public int[] coords;
    public MoveCommand(int[] coords) {
        this.coords = coords;
    }
    
    public void issue(Game game) {
        game.state.move(coords);
    }
}
