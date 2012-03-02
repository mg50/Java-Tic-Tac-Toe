package javattt;

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
    
    public void start(TransitionData data) {
        while(!(state instanceof javattt.fsm.HaltState) && (data == null || data.signal != TransitionData.Signal.PAUSE)) {
            data = state.transition(data);
        }
    }

    //hooks

    public void onNewGame() {};
    public void onSuccessfulMove(int[] coords) {}
    public void onReceivingPlayVsAI() {}
    public void onHalt() {}
    public void onReceivingPlayAsX() {}
}
