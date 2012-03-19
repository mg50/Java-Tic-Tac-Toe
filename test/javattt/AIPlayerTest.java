package javattt;

import javattt.command.MoveCommand;
import javattt.strategy.AIStrategy;
import javattt.ui.Console;
import junit.framework.TestCase;

import static org.junit.Assert.*;

import static javattt.Side.X;
import static javattt.Side.O;
import static javattt.Side._;

public class AIPlayerTest extends TestCase {

    private static Side[][] TestGrid1 = {{X, _, X},
                                         {X, X, O},
                                         {X, _, O}};

    private static Side[][] TestGrid2 = {{O, O, X},
                                         {X, X, O},
                                         {O, X, O}};

    private static Side[][] TestGrid3 = {{O, O, X},
                                         {X, X, O},
                                         {O, X, _}};

    private static Side[][] TestGrid4 = {{O, O, X},
                                         {X, X, O},
                                         {_, X, O}};

    private static Side[][] TestGrid5 = {{O, O, X},
                                         {X, X, O},
                                         {_, X, _}};

    private static Side[][] TestGrid6 = {{_, _, _},
                                         {O, X, _},
                                         {_, _, _}};

    private static Side[][] TestGrid7 = {{X, _, O},
                                         {_, O, _},
                                         {_, _, X}};

    private static Side[][] TestGrid8 = {{X, O, X},
                                         {_, O, _},
                                         {X, _, _}};


    private static Side[][] TestGrid9 = {{X, O, X, O},
                                         {_, O, X, O},
                                         {X, _, _, _},
                                         {X, O, _, _}};

    private static Side[][] TestGrid10 = {{O, _, _, X},
                                          {O, O, O, X},
                                          {_, X, X, _},
                                          {X, _, _, _}};


    public void testLeafValueOne() {
        Board board = new Board(TestGrid1);

        AIStrategy x = new AIStrategy(board.size);
        assertEquals(x.absoluteLeafScore(board), 512);
                
        AIStrategy o = new AIStrategy(board.size);
        assertEquals(o.absoluteLeafScore(board), 512);
    }

    public void testLeafValueTwo() {
        Board board = new Board(TestGrid2);

        AIStrategy x = new AIStrategy(board.size);
        assertEquals(x.absoluteLeafScore(board), 0);

        AIStrategy o = new AIStrategy(board.size);
        assertEquals(o.absoluteLeafScore(board), 0);
    }


    public void testScoreChildOne() {
        Board board = new Board(TestGrid2);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        assertEquals(x.scoreChild(Side.X, board), 0);
        assertEquals(o.scoreChild(Side.O, board), 0);
    }

    public void testScoreChildTwo() {
        Board board = new Board(TestGrid3);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        assertEquals(x.scoreChild(Side.X, board), 0);
        assertEquals(o.scoreChild(Side.O, board), 0);
    }

    public void testScoreChildThree() {
        Board board = new Board(TestGrid4);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        assertEquals(x.scoreChild(Side.X, board), 0);
        assertEquals(o.scoreChild(Side.O, board), 512);
    }

    public void testScoreChildFour() {
        Board board = new Board(TestGrid5);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        assertEquals(x.scoreChild(Side.X, board), 0);
        assertEquals(o.scoreChild(Side.O, board), 512);
    }

    public void testScoreChildFive() {
        Board board = new Board(TestGrid6);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);

        assertEquals(x.scoreChild(Side.X, board), 0);
        assertEquals(o.scoreChild(Side.O, board), 512);
    }

    public void testScoreChildSix() {
        Board board = new Board(TestGrid7);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);

        assertEquals(o.scoreChild(Side.O, board), 512);
    }

    public void testScoreChildSeven() {
        Board board = new Board(TestGrid8);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);

        assertEquals(x.scoreChild(Side.X, board), -512);
        assertEquals(o.scoreChild(Side.O, board), 512);
    }

    public void testCalculateMoveOne() {
        Board board = new Board(TestGrid4);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        MoveCommand cmd1 = (MoveCommand) x.determineNextMove(Side.X, board, new Console());
        MoveCommand cmd2 = (MoveCommand) o.determineNextMove(Side.O, board, new Console());
        assertArrayEquals(cmd1.coords, new int[]{0, 2});
        assertArrayEquals(cmd2.coords, new int[]{0, 2});
    }

    public void testCalculateMoveTwo() {
        Board board = new Board(TestGrid7);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        MoveCommand cmd = (MoveCommand) x.determineNextMove(Side.X, board, new Console());

        assertArrayEquals(cmd.coords, new int[]{0, 2});
    }

    public void testCalculateMoveThree() {
        Board board = new Board(TestGrid8);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        MoveCommand cmd = (MoveCommand) o.determineNextMove(Side.O, board, new Console());

        assertArrayEquals(cmd.coords, new int[]{0, 1});
    }

    public void testCalculateMoveFour() {
        Board board = new Board(TestGrid9);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        MoveCommand cmd1 = (MoveCommand) x.determineNextMove(Side.X, board, new Console());
        MoveCommand cmd2 = (MoveCommand) o.determineNextMove(Side.O, board, new Console());


        assertArrayEquals(cmd1.coords, new int[]{0, 1});
        assertArrayEquals(cmd2.coords, new int[]{1, 2});
    }

    public void testCalculateMoveFive() {
        Board board = new Board(TestGrid10);
        AIStrategy x = new AIStrategy(board.size);
        AIStrategy o = new AIStrategy(board.size);
        MoveCommand cmd1 = (MoveCommand) x.determineNextMove(Side.X, board, new Console());
        MoveCommand cmd2 = (MoveCommand) o.determineNextMove(Side.O, board, new Console());


        assertArrayEquals(cmd1.coords, new int[]{3, 2});
        assertArrayEquals(cmd2.coords, new int[]{0, 2});
    }
}