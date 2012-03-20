package javattt;


import javattt.command.MoveCommand;
import javattt.strategy.HumanStrategy;
import javattt.ui.Console;
import junit.framework.TestCase;

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
        assertArrayEquals(console.parseMove("2 2"), new int[] {1, 1});
        assertArrayEquals(console.parseMove("3 3"), new int[] {2, 2});
    }
    

    public void testPromptMoveOne() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("2 2".getBytes());
        Console console = new Console(inputStream, new ByteArrayOutputStream());
        MoveCommand cmd = (MoveCommand) console.promptMove(new Board());
        assertArrayEquals(cmd.coords, new int[] {1, 1});
    }

    public void testPromptMoveTwo() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("3 1".getBytes());
        Console console = new Console(inputStream, new ByteArrayOutputStream());
        MoveCommand cmd = (MoveCommand) console.promptMove(new Board());
        assertArrayEquals(cmd.coords, new int[] {2, 0});
    }

    public void testPromptMoveThree() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("4 4".getBytes());
        Console console = new Console(inputStream, new ByteArrayOutputStream());
        MoveCommand cmd = (MoveCommand) console.promptMove(new Board(4));
        assertArrayEquals(cmd.coords, new int[] {3, 3});
    }
}
