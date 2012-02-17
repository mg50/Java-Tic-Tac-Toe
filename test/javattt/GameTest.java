package javattt;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;


public class GameTest extends TestCase {
    private static int[][] TestGrid1 = {{2, 0, 2}, {1, 1, 2}, {1, 0, 2}};
    private static int[][] TestGrid2 = {{1, 0, 2}, {1, 1, 2}, {2, 0, 1}};
    private static int[][] TestGrid3 = {{2, 0, 0}, {1, 1, 2}, {1, 0, 2}};
    private static int[][] TestGrid4 = {{2, 2, 1}, {1, 1, 2}, {2, 1, 2}};


    public void testMove() throws Exception {
        Board board = new Board(TestGrid1);
        Player x = new Player(Board.X);
        Game game = new Game(board);
        game.move(1, 0, x);
        int[][] result = {{2, 1, 2}, {1, 1, 2}, {1, 0, 2}};
        assertArrayEquals(board.getGrid(), result);
    }

    public void testOtherPlayer() throws Exception {
        Game game = new Game();
        Player px = new HumanPlayer(Board.X);
        Player po = new HumanPlayer(Board.O);
        
        game.setPlayerX(px);
        game.setPlayerO(po);
        
        assertEquals(game.otherPlayer(px), po);
        assertEquals(game.otherPlayer(po), px);
    }
    
    public void testStartOneGameOne() throws Exception {
        String gameString = "n\ntop left\ntop right\nmiddle left\nmiddle right\nbottom left\n";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertEquals(game.startOneGame(), Board.X);
    }

    public void testStartOneGameTwo() throws Exception {
        String gameString = "n\ncenter\ntop right\nmiddle left\nmiddle right\nbottom left\n\nbottom right";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertEquals(game.startOneGame(), Board.O);
    }

    public void testStartOneGameThree() throws Exception {
        String gameString = "n\ncenter\ncenter\ncenter\ntop right\nmiddle left\nmiddle right\nbottom left\n\nbottom right";
        Game game = new Game();
        game.ui = new Console(game, new ByteArrayInputStream(gameString.getBytes()), new ByteArrayOutputStream());
        assertEquals(game.startOneGame(), Board.O);
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
