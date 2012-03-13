package javattt;

import javattt.strategy.HumanStrategy;
import javattt.ui.Console;
import javattt.ui.MockUI;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocalGame extends Game {

    public final boolean chooseMarkerInPvP = false;

    public LocalGame() {
        Player p = new Player();
        p.ui = new Console();
        p.gameStrategy = new HumanStrategy();
        masterPlayer = p;
        board = new Board();
    }

    /*
    public LocalGame(Board inputBoard) {
        board = inputBoard;
    }

    public LocalGame(Board inputBoard, UI ui) {
        board = inputBoard;
        this.ui = ui;
    }
                        */
    public Board getBoard() {
        return board;
    }

    public boolean chooseMarkerInPvP() {
        playerX = masterPlayer;
        masterPlayer.side = Side.X;

        playerO.gameStrategy = new HumanStrategy();
        playerO.ui = masterPlayer.ui;
        return false;
    }

    public void updatePlayerUIs() {
        masterPlayer.updateUI(board);
    }

    public void displayPlayerHelpMessages() {
        masterPlayer.displayHelp();
    }
    
    public void displayPlayerVictoryMessages(String message) {
        masterPlayer.displayVictoryMessage(message, xWinsCount, oWinsCount);
        
    }

    public void onNewGame() {
        playerX.ui = new MockUI();
        playerO.ui = new MockUI();
    }

    public void onBeginningGame() {
        //masterPlayer.ui = new Console();
        //nonMasterPlayer().ui = masterPlayer.ui;
    }
}
