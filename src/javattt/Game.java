package javattt;

import javattt.command.Command;
import javattt.command.PauseCommand;
import javattt.command.RestartCommand;
import javattt.fsm.*;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/22/12
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class Game {
    public UI ui;
    public Board board = new Board() {};
    public Player playerX;
    public Player playerO;
    public Player currentPlayer;
    public int xWinsCount;
    public int oWinsCount;
    public javattt.fsm.State state = new NewGameState(this);
    public javattt.fsm.State suspendedState;
    public boolean playing = false;

    public void suspend() {
        suspendedState = state;
    }

    public void unsuspend() {
        state = suspendedState;
        suspendedState = null;
    }

    public void start() {
        start(null);
    }

    public void start(Command cmd) {
        while(cmd instanceof RestartCommand || !(state instanceof HaltState || cmd instanceof PauseCommand)) {
            cmd = state.transition(cmd);
            onStateTransition();
        }

        if(state instanceof HaltState) onHalt();
    }

    //hooks

    public void onNewGame() {}
    public void onSuccessfulMove(int[] coords) {}
    public void onReceivingPlayVsAI() {}
    public void onHalt() {}
    public void onReceivingPlayAsX() {}
    public void onGameOver(Side victor) {}
    public void onRestart() {}
    public void onBeginningGame() {}
    public void onStateTransition() {}
}
