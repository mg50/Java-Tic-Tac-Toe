package javattt;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest extends TestCase {

    private static int[][] TestGrid1 = {{2, 0, 2}, {1, 1, 2}, {1, 0, 2}};
    private static int[][] TestGrid2 = {{2, 1, 2}, {1, 1, 2}, {1, 2, 2}};


    @Test
    public void testGetCell() throws Exception {
        Board board1 = new Board();
        assertEquals(board1.getCell(0, 0), Board.Empty);
        
        Board board2 = new Board(TestGrid1);
        assertEquals(board2.getCell(0, 0), Board.O);
    }
    
    @Test
    public void testSetCell() throws Exception {
        Board board1 = new Board();
        board1.setCell(0, 0, Board.O);
        assertEquals(board1.getCell(0, 0), Board.O);

        Board board2 = new Board(TestGrid1);
        board2.setCell(1, 1, Board.Empty);
        assertEquals(board2.getCell(1, 1), Board.Empty);
    }

    @Test
    public void testRows() throws Exception {
        Board board1 = new Board();
        assertArrayEquals(board1.rows()[1], new int[] {0, 0, 0});
        
        Board board2 = new Board(TestGrid1);
        assertArrayEquals(board2.rows()[2], new int[]{1, 0, 2});
    }

    public void testColumns() throws Exception {
        Board board1 = new Board();
        assertArrayEquals(board1.columns()[2], new int[] {0, 0, 0});
        
        Board board2 = new Board(TestGrid1);
        assertArrayEquals(board2.columns()[2], new int[] {2, 2, 2});
    }
    
    public void testDiagonals() throws Exception {
        Board board1 = new Board();
        assertArrayEquals(board1.diagonals()[0], new int[] {0, 0, 0});

        Board board2 = new Board(TestGrid1);
        assertArrayEquals(board2.diagonals()[0], new int[] {2, 1, 2});
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
        Board board = new Board();
        assertTrue(board.equals(board.duplicateBoard()));
    }
    
    public void testEqualsTwo() throws Exception {
        Board board = new Board();
        Board board2 = board.duplicateBoard();
        board2.setCell(0, 0, Board.X);
        assertFalse(board.equals(board2));
    }

    public void testChildNodesOne() {
        Board board = new Board(TestGrid1);
        int[][] c1 = {{2, 1, 2}, {1, 1, 2}, {1, 0, 2}};
        int[][] c2 = {{2, 0, 2}, {1, 1, 2}, {1, 1, 2}};
        Board b1 = new Board(c1);
        Board b2 = new Board(c2);
        Board[] children = {b1, b2};

        assertArrayEquals(board.childNodes(Board.X), children);
    }

    public void testChildNodesTwo() {
        Board board = new Board(TestGrid2);
        assertArrayEquals(board.childNodes(Board.O), new Board[] {});
    }
}
