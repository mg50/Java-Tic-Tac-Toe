package javattt.fsm;

import javattt.strategy.HumanStrategy;
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
        return game.promptPlayer(game.masterPlayer, game.masterPlayer.languageStore.PROMPT_PLAY_VS_AI);
    }

    public void yes() {
        game.state = new PlayAsXState(game);
        game.versusAI = true;
        game.onReceivingPlayVsAI();
    }

    public void no() {
        game.playerX.gameStrategy = new HumanStrategy();
        game.playerO.gameStrategy = new HumanStrategy();
        game.state = game.chooseMarkerInPvP() ? new PlayAsXState(game) : new Play3x3State(game);
        game.versusAI = false;
        game.onReceivingPlayVsAI();
    }

}
