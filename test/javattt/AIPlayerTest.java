package javattt;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AIPlayerTest extends TestCase {

    private static int[][] TestGrid1 = {{2, 0, 1},
                                        {1, 1, 2},
                                        {1, 0, 2}};

    private static int[][] TestGrid2 = {{2, 2, 1},
                                        {1, 1, 2},
                                        {2, 1, 2}};

    private static int[][] TestGrid3 = {{2, 2, 1},
                                        {1, 1, 2},
                                        {2, 1, 0}};

    private static int[][] TestGrid4 = {{2, 2, 1},
                                        {1, 1, 2},
                                        {0, 1, 2}};

    private static int[][] TestGrid5 = {{2, 2, 1},
                                        {1, 1, 2},
                                        {0, 1, 0}};

    private static int[][] TestGrid6 = {{0, 0, 0},
                                        {2, 1, 0},
                                        {0, 0, 0}};

    private static int[][] TestGrid7 = {{1, 0, 2},
                                        {0, 2, 0},
                                        {0, 0, 1}};

    private static int[][] TestGrid8 = {{1, 2, 1},
                                        {0, 2, 0},
                                        {1, 0, 0}};


    public void testLeafValueOne() {
        Board board = new Board(TestGrid1);

        AIPlayer x = new AIPlayer(Board.X);
        assertEquals(x.absoluteLeafScore(board), 1);
                
        AIPlayer o = new AIPlayer(Board.O);
        assertEquals(o.absoluteLeafScore(board), 1);
    }

    public void testLeafValueTwo() {
        Board board = new Board(TestGrid2);

        AIPlayer x = new AIPlayer(Board.X);
        assertEquals(x.absoluteLeafScore(board), 0);

        AIPlayer o = new AIPlayer(Board.O);
        assertEquals(o.absoluteLeafScore(board), 0);
    }


    public void testScoreChildOne() {
        Board board = new Board(TestGrid2);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);
        assertEquals(x.scoreChild(board), (float) 0);
        assertEquals(o.scoreChild(board), (float) 0);
    }

    public void testScoreChildTwo() {
        Board board = new Board(TestGrid3);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);
        assertEquals(x.scoreChild(board), (float) 0);
        assertEquals(o.scoreChild(board), (float) 0);
    }

    public void testScoreChildThree() {
        Board board = new Board(TestGrid4);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);
        assertEquals(x.scoreChild(board), (float) 0);
        assertEquals(o.scoreChild(board), (float) 1);
    }

    public void testScoreChildFour() {
        Board board = new Board(TestGrid5);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);
        assertEquals(x.scoreChild(board), (float) 0);
        assertEquals(o.scoreChild(board), (float) 0.5);
    }

    public void testScoreChildFive() {
        Board board = new Board(TestGrid6);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);

        assertTrue(o.scoreChild(board) > (float) 0);
    }

    public void testScoreChildSix() {
        Board board = new Board(TestGrid7);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);

        assertTrue(o.scoreChild(board) > (float) 0);
    }

    public void testScoreChildSeven() {
        Board board = new Board(TestGrid8);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);

        assertTrue(x.scoreChild(board) < (float) 0);
    }

    public void testCalculateMoveOne() {
        Board board = new Board(TestGrid4);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);
        assertArrayEquals(x.calculateMove(board), new int[]{0, 2});
        assertArrayEquals(o.calculateMove(board), new int[]{0, 2});
    }

    public void testCalculateMoveTwo() {
        Board board = new Board(TestGrid7);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);
        assertArrayEquals(x.calculateMove(board), new int[]{0, 2});
    }

    public void testCalculateMoveThree() {
        Board board = new Board(TestGrid8);
        AIPlayer x = new AIPlayer(Board.X);
        AIPlayer o = new AIPlayer(Board.O);
        assertArrayEquals(o.calculateMove(board), new int[]{1, 2});
    }


}