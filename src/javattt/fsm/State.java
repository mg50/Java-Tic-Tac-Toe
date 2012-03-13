package javattt.fsm;

import javattt.command.Command;
import javattt.command.StepCommand;
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

    public State(Game game) {
        this.game = game;
    }

    public Command readNextCommand() {
        return new StepCommand();
    }
    
    public void step() {}
    public void yes() {}
    public void no() {}
    public void move(int[] coords) {}
    public void victor(Side s) {}
    public void invalid() {}
    public void suspend() {}
    public void restart() {
        game.state = new NewGameState(game);
        game.onRestart();
    }
}
