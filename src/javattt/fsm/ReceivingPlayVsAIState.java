package javattt.fsm;

import javattt.command.Command;
import javattt.command.InvalidCommand;
import javattt.command.NullCommand;
import javattt.Game;
import javattt.HumanPlayer;
import javattt.Side;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivingPlayVsAIState extends State{
    
    public ReceivingPlayVsAIState(Game game) {
        super(game);
    }
    
    public Command yes() {
        game.state = new PromptingPlayAsXState(game);
        game.onReceivingPlayVsAI();
        return new NullCommand();
    }
    
    public Command no() {
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.state = new BeginningGameState(game);
        game.onReceivingPlayVsAI();
        return new NullCommand();
    }

    public Command invalid() {
        game.state = new PromptingPlayVsAIState(game);
        return new InvalidCommand();
    }
}
