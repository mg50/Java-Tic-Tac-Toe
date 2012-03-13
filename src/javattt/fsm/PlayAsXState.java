package javattt.fsm;

import javattt.command.Command;
import javattt.*;
import javattt.strategy.AIStrategy;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayAsXState extends State {
    
    public PlayAsXState(Game game) {
        super(game);
    }
    
    public Command readNextCommand() {
        return game.masterPlayer.ui.prompt(game.masterPlayer.languageStore.PROMPT_PLAY_AS_X);
    }

    public void yes() {
        game.playerX = game.masterPlayer;
        game.masterPlayer.side = Side.X;

        game.playerO.gameStrategy = new AIStrategy(game.board.size);
        finish();
    }
    
    public void no() {
        game.playerX.gameStrategy = new AIStrategy(game.board.size);
        
        game.playerO = game.masterPlayer;
        game.masterPlayer.side = Side.O;
        finish();
    }

    public void finish() {
        game.state = new Play3x3State(game);
        game.onReceivingPlayAsX();
    }
}
