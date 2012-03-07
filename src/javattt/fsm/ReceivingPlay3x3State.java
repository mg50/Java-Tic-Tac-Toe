package javattt.fsm;

import command.Command;
import command.InvalidCommand;
import command.NullCommand;
import javattt.Board;
import javattt.Game;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivingPlay3x3State extends State {
    
    public ReceivingPlay3x3State(Game game) {
        super(game);
    }
        
    public Command yes() {
        game.board = new Board(3);
        game.state = new PromptingPlayVsAIState(game);
        return new NullCommand();
    }
    
    public Command no() {
        game.board = new Board(4);
        game.state = new PromptingPlayVsAIState(game);
        return new NullCommand();
    }

    public Command invalid() {
        game.state = new PromptingPlay3x3State(game);
        return new InvalidCommand();
    }
}
