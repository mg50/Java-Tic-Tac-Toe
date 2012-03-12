package javattt.fsm;

import javattt.command.Command;
import javattt.command.NullCommand;
import javattt.Game;

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
        game.playing = false;
        return new NullCommand();
    }
}
