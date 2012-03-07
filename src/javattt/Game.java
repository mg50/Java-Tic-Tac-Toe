package javattt;

import javattt.command.Command;
import javattt.command.PauseCommand;
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


    public void start() {
        start(null);
    }

    public void start(Command cmd) {
        while(!(state instanceof HaltState) && !(cmd instanceof PauseCommand)) {
            cmd = state.transition(cmd);
        }

        if(state instanceof HaltState) onHalt();
    }

    //hooks

    public void onNewGame() {}
    public void onSuccessfulMove(int[] coords) {}
    public void onReceivingPlayVsAI() {}
    public void onHalt() {}
    public void onReceivingPlayAsX() {}
}
