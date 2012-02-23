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
        DEBUG
    }

    public Signal signal;
    public int[] coords;
    public Side side;
    
    TransitionData(Signal s) {
        if(s == null) signal = Signal.EXIT;
        else signal = s;
    }
    
    TransitionData(int[] coords) {
        signal = Signal.MOVE;
        this.coords = coords;
    }
    
    TransitionData(Boolean b) {
        if(b) signal = Signal.YES;
        else signal = Signal.NO;
    }
    
    TransitionData(Side s) {
        side = s;
    }
}
