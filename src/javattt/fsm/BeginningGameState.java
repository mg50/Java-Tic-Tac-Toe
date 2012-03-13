package javattt.fsm;

import javattt.Game;

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
    
    public void step() {
        game.playing = true;
        game.currentPlayer = game.playerX;
        game.state = new MoveState(game);

        game.updatePlayerUIs();
        game.displayPlayerHelpMessages();

        game.onBeginningGame();
    }
}
