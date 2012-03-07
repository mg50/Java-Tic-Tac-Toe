package javattt.fsm;

import command.Command;
import command.InvalidCommand;
import command.NullCommand;
import javattt.*;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivingPlayAsXState extends State {
    
    public ReceivingPlayAsXState(Game game) {
        super(game);
    }

    public Command yes() {
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new AIPlayer(Side.O, game.board.size);        
        return finish();
    }
    
    public Command no() {
        game.playerX = new AIPlayer(Side.X, game.board.size);
        game.playerO = new HumanPlayer(Side.O);
        return finish();
    }
    
    public Command invalid() {
        game.state = new PromptingPlayAsXState(game);
        return new InvalidCommand();
    }
    
    public Command finish() {
        game.state = new BeginningGameState(game);
        game.onReceivingPlayAsX();
        return new NullCommand();
    }
}
