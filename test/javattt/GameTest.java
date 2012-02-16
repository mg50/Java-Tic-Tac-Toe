package javattt;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;


public class GameTest extends TestCase {
    private static int[][] TestGrid1 = {{2, 0, 2}, {1, 1, 2}, {1, 0, 2}};
    private static int[][] TestGrid2 = {{1, 0, 2}, {1, 1, 2}, {2, 0, 1}};
    private static int[][] TestGrid3 = {{2, 0, 0}, {1, 1, 2}, {1, 0, 2}};
    private static int[][] TestGrid4 = {{2, 2, 1}, {1, 1, 2}, {2, 1, 2}};

    public void testWinnerO() throws Exception {
        Board board1 = new Board(TestGrid1);
        assertTrue(Game.winnerO(board1.columns()[2]));
    }

    public void testWinnerX() throws Exception {
        Board board2 = new Board(TestGrid2);
        assertTrue(Game.winnerX(board2.diagonals()[0]));
    }

    public void testWinner() throws Exception {
        Board board1 = new Board(TestGrid1);
        Game game1 = new Game(board1);
        assertEquals(game1.winner(), Board.O);
        
        Board board2 = new Board(TestGrid2);
        Game game2 = new Game(board2);
        assertEquals(game2.winner(), Board.X);
        
        Board board3 = new Board(TestGrid3);
        Game game3 = new Game(board3);
        assertEquals(game3.winner(), 0);
    }

    
    public void testIsDraw() throws Exception {
        Board board1 = new Board(TestGrid1);
        Game game1 = new Game(board1);
        assertFalse(game1.isDraw());
        
        Board board2 = new Board(TestGrid4);
        Game game2 = new Game(board2);
        assertTrue(game2.isDraw());
    }

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
}
