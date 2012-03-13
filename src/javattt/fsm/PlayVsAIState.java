package javattt.fsm;

import javattt.HumanStrategy;
import javattt.command.Command;
import javattt.Game;
import javattt.Side;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/1/12
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayVsAIState extends State{

    public PlayVsAIState(Game game) {
        super(game);
    }
    
    public Command readNextCommand() {
        return game.masterPlayer.ui.prompt(game.masterPlayer.languageStore.PROMPT_PLAY_VS_AI);
    }

    public void yes() {
        game.state = new PlayAsXState(game);
        game.onReceivingPlayVsAI();
    }

    public void no() {
        game.playerX.gameStrategy = new HumanStrategy(Side.X);
        game.playerO.gameStrategy = new HumanStrategy(Side.O);
        game.state = new BeginningGameState(game);
        game.onReceivingPlayVsAI();
    }

}
