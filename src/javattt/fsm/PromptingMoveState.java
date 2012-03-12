package javattt.fsm;

import javattt.command.Command;
import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class PromptingMoveState extends State {
    
    public PromptingMoveState(Game game) {
        super(game);
    }

    public Command execute() {
        game.state = new ReceivingMoveState(game);
        return game.currentPlayer.determineNextMove(game.board, game.ui);
    }
}