package javattt.fsm;

import javattt.Game;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:56 AM
 * To change this template use File | Settings | Filre Templates.
 */
public class PromptingPlayAsXState extends State {
    
    public PromptingPlayAsXState(Game game) {
        super(game);
    }

    public TransitionData execute() {
        game.state = new ReceivingPlayAsXState(game);
        return game.ui.promptPlayAsX();
    }
}
