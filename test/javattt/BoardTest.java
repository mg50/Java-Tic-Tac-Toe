package javattt;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import static javattt.Side.X;
import static javattt.Side.O;
import static javattt.Side._;

public class BoardTest extends TestCase {

    private static Side[][] TestGrid1 = {{O, _, O},
                                         {X, X, O},
                                         {X, _, O}};

    private static Side[][] TestGrid2 = {{O, X, O},
                                         {X, X, O},
                                         {X, O, O}};

    private static Side[][] TestGrid3 = {{X, O, X},
                                         {O, X, O},
                                         {O, X, O}};

    private static Side[][] TestGrid4 = {{X, X, X},
                                         {O, O, _},
                                         {_, _, _}};

    @Test
    public void testGetCell() throws Exception {
        Board board1 = new Board(3);
        assertEquals(board1.getCell(0, 0), _);
        
        Board board2 = new Board(TestGrid1);
        assertEquals(board2.getCell(0, 0), O);
    }
    
    @Test
    public void testSetCell() throws Exception {
        Board board1 = new Board(3);
        board1.setCell(0, 0, O);
        assertEquals(board1.getCell(0, 0), O);

        Board board2 = new Board(TestGrid1);
        board2.setCell(1, 1, _);
        assertEquals(board2.getCell(1, 1), _);
    }

    @Test
    public void testRows() throws Exception {
        Board board1 = new Board(3);
        assertArrayEquals(board1.rows()[1], new Side[] {_, _, _});
        
        Board board2 = new Board(TestGrid1);
        assertArrayEquals(board2.rows()[2], new Side[]{X, _, O});
    }

    public void testColumns() throws Exception {
        Board board1 = new Board(3);
        assertArrayEquals(board1.columns()[2], new Side[] {_, _, _});
        
        Board board2 = new Board(TestGrid1);
        assertArrayEquals(board2.columns()[2], new Side[] {O, O, O});
    }
    
    public void testDiagonals() throws Exception {
        Board board1 = new Board(3);
        assertArrayEquals(board1.diagonals()[0], new Side[] {_, _, _});

        Board board2 = new Board(TestGrid1);
        assertArrayEquals(board2.diagonals()[0], new Side[] {O, X, O});
    }
    
    public void testHasEmptyCell() throws Exception {
        Board board1 = new Board(TestGrid1);
        assertTrue(board1.hasEmptyCell());
        
        Board board2 = new Board(TestGrid2);
        assertFalse(board2.hasEmptyCell());
    }
    
    public void testEmptyCells() throws Exception {
        Board board = new Board(TestGrid1);
        ArrayList<int[]> emptyCoords = board.emptyCoords();

        assertEquals(emptyCoords.size(), 2);        
        assertArrayEquals(emptyCoords.get(0), new int[]{1, 0});
        assertArrayEquals(emptyCoords.get(1), new int[] {1, 2});
    }
    
    public void testHashCodeOne() throws Exception {
        Board board = new Board(TestGrid1);
        assertEquals(board.hashCode(), 202112102);
    }
    
    public void testHashCodeTwo() throws Exception {
        Board board = new Board(TestGrid2);
        assertEquals(board.hashCode(), 212112122);
    }
    
    public void testEqualsOne() throws Exception {
        Board board = new Board(3);
        assertTrue(board.equals(board.duplicateBoard()));
    }
    
    public void testEqualsTwo() throws Exception {
        Board board = new Board(3);
        Board board2 = board.duplicateBoard();
        board2.setCell(0, 0, X);
        assertFalse(board.equals(board2));
    }

    public void testIsDraw() throws Exception {
        Board board1 = new Board(TestGrid3);
        assertTrue(board1.isDraw());

        Board board2 = new Board(TestGrid2);
        assertFalse(board2.isDraw());
    }

    public void testWinner() throws Exception {
        Board board1 = new Board(TestGrid1);
        assertEquals(board1.winner(), O);

        Board board2 = new Board(TestGrid4);
        assertEquals(board2.winner(), X);

        Board board3 = new Board(TestGrid3);
        assertEquals(board3.winner(), null);
    }

    public void testWinnerTwo() throws Exception {
        Side[][] grid = {{X, X, X, X},
                         {O, O, O, _},
                         {_, _, _, _},
                         {_, _, _, _}};

        Board board = new Board(grid);
        assertEquals(board.winner(), X);
    }
    
    public void testWinnerThree() throws Exception {
        Side[][] grid = {{O, _, _, X},
                         {O, _, _, X},
                         {O, _, _, X},
                         {_, _, _, X}};
        
        Board board = new Board(grid);
        assertEquals(board.winner(), X);
    }

    public void testWinnerFour() throws Exception {
        Side[][] grid = {{O, X, X, X},
                         {X, O, _, _},
                         {_, _, O, _},
                         {_, _, _, O}};

        Board board = new Board(grid);
        assertEquals(board.winner(), O);
    }

    public void testChildNodesOne() {
        Board board = new Board(TestGrid1);
        Side[][] c1 = {{O, X, O}, {X, X, O}, {X, _, O}};
        Side[][] c2 = {{O, _, O}, {X, X, O}, {X, X, O}};
        Board b1 = new Board(c1);
        Board b2 = new Board(c2);
        Board[] children = {b1, b2};

        assertArrayEquals(board.childNodes(X), children);
    }



    public void testChildNodesTwo() {
        Board board = new Board(TestGrid2);
        assertArrayEquals(board.childNodes(O), new Board[] {});
    }
}
