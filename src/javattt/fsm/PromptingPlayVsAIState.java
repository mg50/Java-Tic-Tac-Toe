package javattt.fsm;

import javattt.command.Command;
import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class PromptingPlayVsAIState extends State {
    public PromptingPlayVsAIState(Game game) {
        super(game);
    }

    public Command execute() {
        game.state = new ReceivingPlayVsAIState(game);
        return game.ui.promptPlayVsAI();
    }
}