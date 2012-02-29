package javattt;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;
import static javattt.Side.X;
import static javattt.Side.O;
import static javattt.Side._;

public class LocalGameTest extends TestCase {

    public void test3x3Prompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        assertEquals(game.stage, Stage.newGame);

        TransitionData data = game.transition(null);
        assertEquals(game.stage, Stage.receivingPlay3x3);
        game.transition(data);
        assertEquals(game.board.size, 3);
    }

    public void test3x3PromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        assertEquals(game.stage, Stage.newGame);

        TransitionData data = game.transition();
        assertEquals(game.stage, Stage.receivingPlay3x3);
        game.transition(data);
        assertEquals(game.board.size, 4);
    }

    public void test3x3PromptThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("b\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData result = game.transition();
        game.transition(result);

        assertEquals(game.stage, Stage.receivingPlay3x3);
    }



    public void testAIPromptOne() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\ny\ny\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData result = game.transition();
        result = game.transition(result);
        result = game.transition(result);
        result = game.transition(result);

        assertEquals(game.stage, Stage.receivingPlayAsX);
    }

    public void testAIPromptTwo() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\ny\nn\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData result = game.transition();
        result = game.transition(result);
        result = game.transition(result);
        result = game.transition(result);
        assertEquals(game.stage, Stage.receivingPlayAsX);
    }

    public void testChoosePlayerPromptOne() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\nn\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData data = game.transition();
        data = game.transition(data);
        data = game.transition(data);
        assertEquals(game.stage, Stage.queryingMove);
        assertTrue(game.playerX instanceof HumanPlayer);
        assertTrue(game.playerO instanceof HumanPlayer);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testChoosePlayerPromptTwo() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("exit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.stage = Stage.receivingPlayAsX;
        TransitionData data = new TransitionData(true);
        game.transition(data);
        assertEquals(game.stage, Stage.queryingMove);
        assertTrue(game.playerX instanceof HumanPlayer);
        assertTrue(game.playerO instanceof AIPlayer);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testChoosePlayerPromptThree() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("exit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.stage = Stage.receivingPlayAsX;
        TransitionData data = new TransitionData(false);
        game.transition(data);
        assertEquals(game.stage, Stage.queryingMove);
        assertTrue(game.playerX instanceof AIPlayer);
        assertTrue(game.playerO instanceof HumanPlayer);
        assertEquals(game.currentPlayer, game.playerX);
    }


    public void testMakeMoveOne() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("exit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.currentPlayer = game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.stage = Stage.queryingMove;
        game.transition();
        assertEquals(game.stage, Stage.receivingMove);
    }


    public void testMakeMoveTwo() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("1 3\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.currentPlayer = game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.stage = Stage.queryingMove;
        TransitionData move = game.transition();
        game.transition(move);
        assertEquals(game.stage, Stage.queryingMove);
        assertEquals(game.board.getCell(0, 2), Side.X);
        assertEquals(game.board.getCell(2, 2), Side._);
    }

    public void testMakeMoveThree() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("2 2\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.playerX = new HumanPlayer(Side.X);
        game.currentPlayer = game.playerO = new HumanPlayer(Side.O);
        game.stage = Stage.queryingMove;
        TransitionData move = game.transition();
        game.transition(move);
        assertEquals(game.stage, Stage.queryingMove);
        assertEquals(game.board.getCell(1, 1), Side.O);
        assertEquals(game.board.getCell(2, 0), Side._);
    }

    public void testMakeMoveFour() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("1 2\n3 2\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.currentPlayer = game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.stage = Stage.queryingMove;
        TransitionData move = game.transition();
        move = game.transition(move);
        move = game.transition(move);
        move = game.transition(move);

        assertEquals(game.stage, Stage.queryingMove);
        assertEquals(game.board.getCell(0, 1), Side.X);
        assertEquals(game.board.getCell(2, 1), Side.O);
        assertEquals(game.board.getCell(1, 0), Side._);
    }


    public void testEndGame() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Board board = new Board(new Side[][] {{X, O, X}, {O, X, O}, {X, O, _}});
        Console ui = new Console(new ByteArrayInputStream("3 3\ny\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = board;
        game.currentPlayer = game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.stage = Stage.queryingMove;
        TransitionData move = game.transition();
        TransitionData victor = game.transition(move);

        assertEquals(game.stage, Stage.gameOver);
        assertEquals(victor.side, Side.X);

        game.transition(victor);

        assertEquals(game.stage, Stage.promptingStarNewGame);
        assertEquals(game.xWinsCount, 1);
        assertEquals(game.oWinsCount, 0);
    }

    public void test4x4TwoPlayerOne() {
        Side[][] grid = {{X, O, O, X}, {_, O, O, O}, {X, _, _, _}, {X, _, _, _}};
        Board board = new Board(grid);
        Console ui = new Console(new ByteArrayInputStream("1 2\n".getBytes()), new ByteArrayOutputStream());
        Game game = new LocalGame(ui);
        game.board = board;
        game.currentPlayer = game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.stage = Stage.queryingMove;
        TransitionData move = game.transition();
        TransitionData victor = game.transition(move);

        assertEquals(game.stage, Stage.gameOver);
        assertEquals(victor.side, Side.X);
    }

    public void test4x4TwoPlayerTwo() {
        Side[][] grid = {{X, O, O, X}, {_, O, O, O}, {X, _, _, _}, {X, _, _, _}};
        Board board = new Board(grid);
        Console ui = new Console(new ByteArrayInputStream("1 2\n".getBytes()), new ByteArrayOutputStream());
        Game game = new LocalGame(ui);
        game.board = board;
        game.playerX = new HumanPlayer(Side.X);
        game.currentPlayer = game.playerO = new HumanPlayer(Side.O);
        game.stage = Stage.queryingMove;
        TransitionData move = game.transition();
        TransitionData victor = game.transition(move);

        assertEquals(game.stage, Stage.gameOver);
        assertEquals(victor.side, Side.O);
    }
}