package javattt.fsm;

import javattt.Game;
import javattt.TransitionData;

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
    
    public TransitionData yes() {
        game.state = new NewGameState(game);
        return null;
    }
    
    public TransitionData no() {
        game.state = new HaltState(game);
        return null;
    }
    
    public TransitionData invalid() {
        game.state = new PromptingStartNewGameState(game);
        return null;
    }
}
