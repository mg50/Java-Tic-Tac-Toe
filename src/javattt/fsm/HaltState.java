package javattt.fsm;

import javattt.Game;
import javattt.TransitionData;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class HaltState extends State {
    public HaltState(Game game) {
        super(game);
    }
    
    public TransitionData pass() {
        return null;
    }
}
