package javattt.fsm;

import javattt.command.Command;
import javattt.Game;
import javattt.Side;
import javattt.command.VictorCommand;

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
    
    public Command readNextCommand() {
        return new VictorCommand(game.board.winner());
    }

    public void step() {
        game.playing = false;
        game.state = new StartNewGameState(game);
        game.onGameOver(null);
    }

    public void victor(Side side) {
        game.playing = false;
        if(side == Side.X) game.xWinsCount++;
        else if(side == Side.O) game.oWinsCount++;
        //game.ui.victoryMessage(side, game.xWinsCount, game.oWinsCount);

        game.state = new StartNewGameState(game);

        game.onGameOver(side);
    }
}
