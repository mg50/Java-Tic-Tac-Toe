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
        String pxMessage, poMessage;
        if(side == Side.X) {
            game.xWinsCount++;
            pxMessage = game.playerX.languageStore.PLAYER_X_WON;
            poMessage = game.playerO.languageStore.PLAYER_X_WON;
        }
        else if(side == Side.O) {
            game.oWinsCount++;
            pxMessage = game.playerX.languageStore.PLAYER_O_WON;
            poMessage = game.playerO.languageStore.PLAYER_O_WON;
        }
        else {
            pxMessage = game.playerX.languageStore.DRAW;
            poMessage = game.playerO.languageStore.DRAW;

        }
        game.playerX.ui.victoryMessage(pxMessage, game.xWinsCount, game.oWinsCount);
        game.playerO.ui.victoryMessage(poMessage, game.xWinsCount, game.oWinsCount);

        game.state = new StartNewGameState(game);

        game.onGameOver(side);
    }
}
