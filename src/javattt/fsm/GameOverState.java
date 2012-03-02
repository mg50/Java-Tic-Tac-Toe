package javattt.fsm;

import javattt.Game;
import javattt.Side;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameOverState extends State {
    public GameOverState(Game game) {
        super(game);
    }

    public TransitionData pass() {
        game.state = new PromptingStartNewGameState(game);
        return null;
    }

    public TransitionData victor(Side side) {
        if(side == Side.X) game.xWinsCount++;
        else if(side == Side.O) game.oWinsCount++;
        game.ui.victoryMessage(side, game.xWinsCount, game.oWinsCount);

        game.state = new PromptingStartNewGameState(game);
        return null;
    }
}
