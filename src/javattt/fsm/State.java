package javattt.fsm;

import javattt.Game;
import javattt.Side;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:06 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class State {
    public Game game;
    
    public TransitionData transition() {
        return transition(null);
    }
    
    public TransitionData transition(TransitionData data) {
        if(data == null) return pass();

        TransitionData ret = null;
        switch(data.signal) {
            case YES:
                ret = yes();
                break;
            case NO:
                ret = no();
                break;
            case MOVE:
                ret = move(data.coords);
                break;
            case VICTOR:
                ret = victor(data.side);
                break;
            case INVALID:
                ret = invalid();
                break;
            case EXIT:
                game.onHalt();
                break;
        }

        return ret;
    }
    
    public State(Game game) {
        this.game = game;
    }
    
    public TransitionData move(TransitionData move) {
        return null;
    };
    
    public TransitionData pass() {return null;}
    public TransitionData yes() {return null;}
    public TransitionData no() {return null;}
    public TransitionData move(int[] coords) {return null;}
    public TransitionData victor(Side s) {return null;}
    public TransitionData invalid() {return null;}
}
