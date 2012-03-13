package javattt.strategy;

import javattt.Board;
import javattt.Side;
import javattt.ui.UI;
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

    public GameStrategy() {

    }

    public Command determineNextMove(Side side, Board board, UI ui) { // No-op
        return new StepCommand();
    }
}
