package javattt;

import command.Command;
import command.NullCommand;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Player {

    public final Side side;

    public Player(Side side) {
        this.side = side;
    }

    public Command determineNextMove(Board board, UI ui) { // No-op
        return new NullCommand();
    }
}
