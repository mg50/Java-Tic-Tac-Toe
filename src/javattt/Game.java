package javattt;

import javattt.command.Command;
import javattt.command.PauseCommand;
import javattt.command.RestartCommand;
import javattt.command.StepCommand;
import javattt.fsm.*;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/22/12
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class Game {
    public Board board = new Board() {};
    public Player playerX = new Player(Side.X);
    public Player playerO = new Player(Side.O);
    public Player currentPlayer;
    public Player masterPlayer;
    public int xWinsCount;
    public int oWinsCount;
    public javattt.fsm.State state = new NewGameState(this);
    public boolean playing = false;
    public boolean versusAI;

    public void start() {
        start(null);
    }

    public void start(Command cmd) {
        while(cmd instanceof RestartCommand || !(state instanceof HaltState || cmd instanceof PauseCommand)) {
            if(cmd == null) cmd = new StepCommand();
            cmd.issue(this);
            cmd = state.readNextCommand();
            onStateTransition();
        }

        if(state instanceof HaltState) onHalt();
    }

    public boolean chooseMarkerInPvP() {
        return true;
    }

    public void updatePlayerUIs() {
        playerX.updateUI(board);
        playerO.updateUI(board);
    }    

    public void displayPlayerHelpMessages() {
        playerX.displayHelp();
        playerO.displayHelp();
    }

    public void displayPlayerVictoryMessages(String message) {
        playerX.displayVictoryMessage(message, xWinsCount, oWinsCount);
        playerO.displayVictoryMessage(message, xWinsCount, oWinsCount);
    }

    public Command promptPlayer(Player player, String message) {
        return player.promptUI(message);
    }
    
    
    public Player nonMasterPlayer() {
        return masterPlayer == playerX ? playerO : playerX;
    }



    //hooks

    public void onNewGame() {}
    public void onSuccessfulMove(int[] coords) {}
    public void onReceivingPlayVsAI() {}
    public void onHalt() {}
    public void onReceivingPlayAsX() {}
    public void onGameOver(Side victor) {}
    public void onRestart() {}
    public void onBeginningGame() {}
    public void onStateTransition() {}
}
