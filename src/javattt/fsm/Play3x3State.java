package javattt.fsm;

import javattt.command.Command;
import javattt.Board;
import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Play3x3State extends State {
    
    public Play3x3State(Game game) {
        super(game);
    }
    
    public Command readNextCommand() {
        return game.promptPlayer(game.masterPlayer, game.masterPlayer.languageStore.PROMPT_PLAY_3x3);
    }
        
    public void yes() {
        game.board = new Board(3);
        game.state = new BeginningGameState(game);
    }
    
    public void no() {
        game.board = new Board(4);
        game.state = new BeginningGameState(game);
    }
}
