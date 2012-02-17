package javattt;


import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/15/12
 * Time: 8:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleTest extends TestCase {

    public void testParseMove() {
        Console console = new Console();
        assertArrayEquals(console.parseMove("center"), new int[] {1, 1});
        assertArrayEquals(console.parseMove("bottom right"), new int[] {2, 2});
    }
    
    public void testPromptMoveOne() {
        Game game = new Game();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("center".getBytes());
        HumanPlayer player = new HumanPlayer(Board.X);
        Console console = new Console(game, inputStream, new ByteArrayOutputStream());
        assertArrayEquals(console.promptPlayer(game.getBoard()), new int[] {1, 1});
    }

    public void testPromptMoveTwo() {
        Game game = new Game();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("top right".getBytes());
        HumanPlayer player = new HumanPlayer(Board.X);
        Console console = new Console(game, inputStream, new ByteArrayOutputStream());
        assertArrayEquals(console.promptPlayer(game.getBoard()), new int[] {2, 0});
    }

    public void testPromptMoveThree() {
        Game game = new Game();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("invalid\ntop right".getBytes());
        HumanPlayer player = new HumanPlayer(Board.X);
        Console console = new Console(game, inputStream, new ByteArrayOutputStream());
        assertArrayEquals(console.promptPlayer(game.getBoard()), new int[] {2, 0});
    }

    public void testPromptMoveFour() {
        int[][] grid = {{1, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        Board board = new Board(grid);
        Game game = new Game(board);
        ByteArrayInputStream inputStream = new ByteArrayInputStream("top left\ncenter".getBytes());
        HumanPlayer player = new HumanPlayer(Board.X);
        Console console = new Console(game, inputStream, new ByteArrayOutputStream());
        assertArrayEquals(console.promptPlayer(game.getBoard()), new int[] {1, 1});
    }
}
