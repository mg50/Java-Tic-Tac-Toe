package javattt;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/23/12
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class TransitionData {

    public Boolean bool;
    public int[] move;
    public Side side;
    
    TransitionData(Boolean b) {
        bool = b;
    }
    
    TransitionData(int[] move) {
        this.move = move;
    }
    
    TransitionData(Side s) {
        side = s;
    }
    
}
