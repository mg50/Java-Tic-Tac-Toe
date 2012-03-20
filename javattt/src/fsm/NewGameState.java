package javattt.fsm;

import javattt.Game;
import javattt.Player;
import javattt.Side;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameState extends State {
    
    public NewGameState(Game game) {
        super(game);
    }

    public void step() {
        game.playerX = new Player(Side.X);
        game.playerO = new Player(Side.O);
        game.currentPlayer = null;
        game.state = new PlayVsAIState(game);
        game.onNewGame();
    }
}
