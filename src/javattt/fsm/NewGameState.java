package javattt.fsm;

import javattt.command.Command;
import javattt.command.NullCommand;
import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameState extends State {
    
    public NewGameState(Game game) {
        super(game);
    }

    public Command execute() {
        game.playerX = null;
        game.playerO = null;
        game.currentPlayer = null;
        game.state = new PromptingPlay3x3State(game);
        game.onNewGame();
        return new NullCommand();
    }
}