package javattt.fsm;

import command.Command;
import command.NullCommand;
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
    
    public Command execute() {
        return new NullCommand();
    }
}
