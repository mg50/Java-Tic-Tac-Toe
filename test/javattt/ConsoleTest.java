package javattt;


import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

import static javattt.Side.X;
import static javattt.Side.O;
import static javattt.Side._;

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
        ByteArrayInputStream inputStream = new ByteArrayInputStream("center".getBytes());
        HumanPlayer player = new HumanPlayer(Side.X);
        Console console = new Console(inputStream, new ByteArrayOutputStream());
        Game game = new Game(console);
        assertArrayEquals(console.promptMove(game.getBoard()), new int[] {1, 1});
    }

    public void testPromptMoveTwo() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("top right".getBytes());
        HumanPlayer player = new HumanPlayer(Side.X);
        Console console = new Console(inputStream, new ByteArrayOutputStream());
        Game game = new Game(console);
        assertArrayEquals(console.promptMove(game.getBoard()), new int[] {2, 0});
    }

    public void testPromptMoveThree() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("invalid\ntop right".getBytes());
        HumanPlayer player = new HumanPlayer(Side.X);
        Console console = new Console(inputStream, new ByteArrayOutputStream());
        Game game = new Game(console);

        assertArrayEquals(console.promptMove(game.getBoard()), new int[] {2, 0});
    }

    public void testPromptMoveFour() {
        Side[][] grid = {{X, _, _}, {_, _, _}, {_, _, _}};
        Board board = new Board(grid);
        ByteArrayInputStream inputStream = new ByteArrayInputStream("top left\ncenter".getBytes());
        HumanPlayer player = new HumanPlayer(Side.X);
        Console console = new Console(inputStream, new ByteArrayOutputStream());
        Game game = new Game(board, console);

        assertArrayEquals(console.promptMove(game.getBoard()), new int[] {1, 1});
    }
}
