package javattt.fsm;

import command.Command;
import command.NullCommand;
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

    public Command execute() {
        game.state = new PromptingStartNewGameState(game);
        return new NullCommand();
    }

    public Command victor(Side side) {
        if(side == Side.X) game.xWinsCount++;
        else if(side == Side.O) game.oWinsCount++;
        game.ui.victoryMessage(side, game.xWinsCount, game.oWinsCount);

        game.state = new PromptingStartNewGameState(game);
        return new NullCommand();
    }
}
