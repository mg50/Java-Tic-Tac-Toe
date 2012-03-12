package javattt.fsm;

import javattt.command.Command;
import javattt.command.NullCommand;
import javattt.Game;
import javattt.Side;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:06 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class State {
    public Game game;
    
    public Command transition() {
        return transition(new NullCommand());
    }
    
    public Command transition(Command cmd) {
        if(cmd == null) cmd = new NullCommand();
        return cmd.sendToGame(game);
    }
    
    public State(Game game) {
        this.game = game;
    }

    public Command execute() {return null;}
    public Command yes() {return null;}
    public Command no() {return null;}
    public Command move(int[] coords) {return null;}
    public Command victor(Side s) {return null;}
    public Command invalid() {return null;}
    public Command suspend() {return null;}
    public Command restart() {
        game.state = new NewGameState(game);
        game.onRestart();
        return new NullCommand();
    }
}
