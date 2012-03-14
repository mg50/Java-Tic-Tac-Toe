package javattt.fsm;

import javattt.Game;
import javattt.command.Command;
import javattt.command.PauseCommand;
import javattt.command.StepCommand;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeginningGameState extends State {
    public BeginningGameState(Game game) {
        super(game);
    }

    public Command readNextCommand() {
        if(game.readyForGameStart()) return new StepCommand();
        else return new PauseCommand();
    }

    public void step() {
        if(game.readyForGameStart()) {
            game.playing = true;
            game.currentPlayer = game.playerX;

            game.updatePlayerUIs();
            game.displayPlayerHelpMessages();


            game.state = new MoveState(game) ;
        }
    }
}
