package javattt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocalGame extends Game {

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

    public void onNewGame() {
        masterPlayer.ui = new Console();
        playerX.ui = new MockUI();
        playerO.ui = new MockUI();
    }
}
