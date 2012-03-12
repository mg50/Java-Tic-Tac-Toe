package javattt.fsm;

import javattt.command.Command;
import javattt.command.NullCommand;
import javattt.command.VictorCommand;
import javattt.Game;
import javattt.Side;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivingMoveState extends State {
    public ReceivingMoveState(Game game) {
        super(game);
    }
    
    public Command invalid() {
        game.state = new PromptingMoveState(game);
        return new NullCommand();
    }

    public Command move(int[] coords) {
        int x = coords[0];
        int y = coords[1];
        Command ret = null;
        if(game.board.getCell(x, y) == Side._) {
            game.board.setCell(x, y, game.currentPlayer.side);
            game.ui.update(game.board);
            game.currentPlayer = game.currentPlayer == game.playerX ? game.playerO : game.playerX;
            
            Side winner = game.board.winner();
            if(winner == null && !game.board.isDraw()) {
                game.state = new PromptingMoveState(game);
                ret = new NullCommand();
            }
            else {
                game.state = new GameOverState(game);
                ret = new VictorCommand(winner);
            }

            game.onSuccessfulMove(coords);
            return ret;
        }
        else {
            return invalid();
        }
    }
    
    public Command execute() {
        return invalid();
    }
}
