package javattt;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

import static javattt.Side.X;
import static javattt.Side.O;
import static javattt.Side._;

public class GameTest extends TestCase {
    private static Side[][] TestGrid1 = {{O, _, O}, {X, X, O}, {X, _, O}};
    private static Side[][] TestGrid2 = {{X, _, O}, {X, X, O}, {O, _, X}};
    private static Side[][] TestGrid3 = {{O, _, _}, {X, X, O}, {X, _, O}};
    private static Side[][] TestGrid4 = {{O, O, X}, {X, X, O}, {O, X, O}};


    public void testMove() throws Exception {
        Board board = new Board(TestGrid1);
        Player x = new HumanPlayer(X);
        Game game = new Game(board);
        game.move(1, 0, x);
        Side[][] result = {{O, X, O}, {X, X, O}, {X, _, O}};
        assertArrayEquals(board.getGrid(), result);
    }

    public void testStartOneGameOne() throws Exception {
        String gameString = "n\ntop left\ntop right\nmiddle left\nmiddle right\nbottom left\n";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertEquals(game.startOneGame(), X);
    }

    public void testStartOneGameTwo() throws Exception {
        String gameString = "n\ncenter\ntop right\nmiddle left\nmiddle right\nbottom left\n\nbottom right";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertEquals(game.startOneGame(), O);
    }

    public void testStartOneGameThree() throws Exception {
        String gameString = "n\ncenter\ncenter\ncenter\ntop right\nmiddle left\nmiddle right\nbottom left\n\nbottom right";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertEquals(game.startOneGame(), O);
    }
    
    public void testStartOne() throws Exception {
        String gameString = "n\ntop left\ntop right\nmiddle left\nmiddle right\nbottom left\nn\n";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertArrayEquals(game.start(), new int[] {1, 0, 0});
    }

    public void testStartTwo() throws Exception {
        String gameString1 = "n\ntop left\ntop right\nmiddle left\nmiddle right\nbottom left\n";
        String gameString2 = "n\ncenter\ncenter\ncenter\ntop right\nmiddle left\nmiddle right\nbottom left\n\nbottom right\n";

        String gameString = gameString1 + "y\n" + gameString2 + "n\n";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertArrayEquals(game.start(), new int[] {1, 1, 0});
    }
}
