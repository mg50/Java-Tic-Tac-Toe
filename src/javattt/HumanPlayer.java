package javattt;

import javattt.command.Command;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class HumanPlayer extends Player {
    public HumanPlayer(Side side) {
        super(side);
    }
    
    public Command determineNextMove(Board board, UI ui) {
        return ui.promptMove(board);
    }
}
