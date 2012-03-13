package javattt.fsm;

import javattt.command.Command;
import javattt.Game;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class StartNewGameState extends State {

    public StartNewGameState(Game game) {
        super(game);
    }
    
    public Command readNextCommand() {
        return game.masterPlayer.ui.prompt(game.currentPlayer.languageStore.PROMPT_START_NEW_GAME);
    }
    
    public void yes() {
        game.state = new NewGameState(game);
    }
    
    public void no() {
        game.state = new HaltState(game);
    }
}
