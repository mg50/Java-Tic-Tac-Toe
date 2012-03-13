package javattt;

import javattt.strategy.GameStrategy;
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
    public UI ui;
    public LanguageStore languageStore = LanguageStore.instance;

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
