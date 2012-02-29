package javattt;

import sun.font.TrueTypeFont;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/23/12
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class TransitionData {

    public enum Signal {
        YES,
        NO,
        EXIT,
        MOVE,
        INVALID,
        VICTOR,
        PAUSE,
        RESTART
    }

    public Signal signal;
    public int[] coords;
    public Side side;
    
    public TransitionData(Signal s) {
        if(s == null) signal = Signal.EXIT;
        else signal = s;
    }
    
    public TransitionData(int[] coords) {
        signal = Signal.MOVE;
        this.coords = coords;
    }
    
    public TransitionData(Boolean b) {
        if(b) signal = Signal.YES;
        else signal = Signal.NO;
    }
    
    public TransitionData(Side s) {
        side = s;
    }
}
