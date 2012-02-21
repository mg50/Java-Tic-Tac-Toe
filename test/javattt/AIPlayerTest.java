package javattt;

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


    public void testLeafValueOne() {
        Board board = new Board(TestGrid1);

        AIPlayer x = new AIPlayer(Side.X);
        assertEquals(x.absoluteLeafScore(board), 512);
                
        AIPlayer o = new AIPlayer(Side.O);
        assertEquals(o.absoluteLeafScore(board), 512);
    }

    public void testLeafValueTwo() {
        Board board = new Board(TestGrid2);

        AIPlayer x = new AIPlayer(Side.X);
        assertEquals(x.absoluteLeafScore(board), 0);

        AIPlayer o = new AIPlayer(Side.O);
        assertEquals(o.absoluteLeafScore(board), 0);
    }


    public void testScoreChildOne() {
        Board board = new Board(TestGrid2);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);
        assertEquals(x.scoreChild(board), 0);
        assertEquals(o.scoreChild(board), 0);
    }

    public void testScoreChildTwo() {
        Board board = new Board(TestGrid3);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);
        assertEquals(x.scoreChild(board), 0);
        assertEquals(o.scoreChild(board), 0);
    }

    public void testScoreChildThree() {
        Board board = new Board(TestGrid4);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);
        assertEquals(x.scoreChild(board), 0);
        assertEquals(o.scoreChild(board), 256);
    }

    public void testScoreChildFour() {
        Board board = new Board(TestGrid5);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);
        assertEquals(x.scoreChild(board), 0);
        assertEquals(o.scoreChild(board), 256);
    }

    public void testScoreChildFive() {
        Board board = new Board(TestGrid6);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);

        assertEquals(x.scoreChild(board), 0);
        assertEquals(o.scoreChild(board), 16);
    }

    public void testScoreChildSix() {
        Board board = new Board(TestGrid7);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);

        assertEquals(o.scoreChild(board), 64);
    }

    public void testScoreChildSeven() {
        Board board = new Board(TestGrid8);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);

        assertEquals(x.scoreChild(board), -256);
        assertEquals(o.scoreChild(board), 256);
    }

    public void testCalculateMoveOne() {
        Board board = new Board(TestGrid4);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);
        assertArrayEquals(x.calculateMove(board), new int[]{0, 2});
        assertArrayEquals(o.calculateMove(board), new int[]{0, 2});
    }

    public void testCalculateMoveTwo() {
        Board board = new Board(TestGrid7);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);
        assertArrayEquals(x.calculateMove(board), new int[]{0, 2});
    }

    public void testCalculateMoveThree() {
        Board board = new Board(TestGrid8);
        AIPlayer x = new AIPlayer(Side.X);
        AIPlayer o = new AIPlayer(Side.O);
        assertArrayEquals(o.calculateMove(board), new int[]{1, 2});
    }


}