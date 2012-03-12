package javattt.fsm;

import javattt.command.Command;
import javattt.command.NullCommand;
import javattt.Game;
import javattt.Side;

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
        game.playing = false;
        game.state = new PromptingStartNewGameState(game);
        game.onGameOver(null);
        return new NullCommand();
    }

    public Command victor(Side side) {
        game.playing = false;
        if(side == Side.X) game.xWinsCount++;
        else if(side == Side.O) game.oWinsCount++;
        game.ui.victoryMessage(side, game.xWinsCount, game.oWinsCount);

        game.state = new PromptingStartNewGameState(game);

        game.onGameOver(side);
        return new NullCommand();
    }
}
