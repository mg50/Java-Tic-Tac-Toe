package javattt;

import javattt.command.Command;
import javattt.strategy.GameStrategy;
import javattt.ui.MockUI;
import javattt.ui.UI;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/12/12
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    public GameStrategy gameStrategy;
    public Side side;
    public UI ui = new MockUI();
    public LanguageStore languageStore = LanguageStore.instance;

    public Command promptUI(String message) {
        return ui.prompt(message);
    }
    
    public void updateUI(Board board) {
        ui.update(board);
    }

    public void displayHelp() {
        ui.displayHelp();
    }
    
    public void displayVictoryMessage(String message, int xWinsCount, int oWinsCount) {
        ui.victoryMessage(message, xWinsCount, oWinsCount);
    }
    
    public Player() {

    }

    public Player(Side side) {
        this.side = side;
    }
    
    public Player(Side side, UI ui) {
        this.side = side;
        this.ui = ui;
    }
}
