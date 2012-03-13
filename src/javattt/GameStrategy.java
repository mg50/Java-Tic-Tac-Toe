package javattt;

import javattt.command.Command;
import javattt.command.StepCommand;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GameStrategy {

    public Side side;

    public GameStrategy() {

    }

    public GameStrategy(Side side) {
        this.side = side;
    }

    public Command determineNextMove(Side side, Board board, UI ui) { // No-op
        return new StepCommand();
    }
}
