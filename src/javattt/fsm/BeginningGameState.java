package javattt.fsm;

import command.Command;
import command.NullCommand;
import javattt.Game;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeginningGameState extends State {
    public BeginningGameState(Game game) {
        super(game);
    }
    
    public Command execute() {
        game.currentPlayer = game.playerX;
        game.ui.update(game.board);
        game.ui.displayHelp();
        game.state = new PromptingMoveState(game);
        return new NullCommand();
    }
}
