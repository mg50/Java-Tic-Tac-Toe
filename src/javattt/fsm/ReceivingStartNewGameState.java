package javattt.fsm;

import javattt.command.Command;
import javattt.command.InvalidCommand;
import javattt.command.NullCommand;
import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivingStartNewGameState extends State {

    public ReceivingStartNewGameState(Game game) {
        super(game);
    }
    
    public Command yes() {
        game.state = new NewGameState(game);
        return new NullCommand();
    }
    
    public Command no() {
        game.state = new HaltState(game);
        return new NullCommand();
    }
    
    public Command invalid() {
        game.state = new PromptingStartNewGameState(game);
        return new InvalidCommand();
    }
}
