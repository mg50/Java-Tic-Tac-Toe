package javattt.strategy;

import javattt.Board;
import javattt.Side;
import javattt.ui.UI;
import javattt.command.Command;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class HumanStrategy extends GameStrategy {

    public HumanStrategy() {

    }


    public Command determineNextMove(Side side, Board board, UI ui) {
        return ui.promptMove(board);
    }
}
