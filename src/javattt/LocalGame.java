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

    public LocalGame(UI ui) {
        board = new Board();
        this.ui = ui;
    }
    public LocalGame(Board inputBoard) {
        board = inputBoard;
    }

    public LocalGame(Board inputBoard, UI ui) {
        board = inputBoard;
        this.ui = ui;
    }

    public Board getBoard() {
        return board;
    }


    public void move(int x, int y, Player player) {
        board.setCell(x, y, player.side);
    }
}
