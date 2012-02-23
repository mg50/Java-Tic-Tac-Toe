package javattt;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;
import static javattt.Side.X;
import static javattt.Side.O;
import static javattt.Side._;

public class LocalGameTest extends TestCase {





    public void testNewGameStage() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.transition();
        assertEquals(game.stage, Stage.receivingPlayVsAI);
    }
        /*

    public void testNewGameTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("b\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData result = game.transition();
        game.transition(result);
        assertEquals(game.stage, Stage.newGame);
    }
       */

    public void testNewGameThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);

        TransitionData result = game.transition();
        assertEquals(game.stage, Stage.receivingPlayVsAI);
        game.transition(result);
        assertEquals(game.stage, Stage.queryingMove);
    }


    public void testAIPromptOne() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\ny\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData result = game.transition();
        result = game.transition(result);
        result = game.transition(result);
        assertEquals(game.stage, Stage.receivingPlayAsX);
    }

    public void testAIPromptTwo() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\nn\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData result = game.transition();
        game.transition(result);
        assertEquals(game.stage, Stage.receivingPlayAsX);
    }

    public void testChoosePlayerPromptOne() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);
        TransitionData data = game.transition();
        game.transition(data);
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
        Console ui = new Console(new ByteArrayInputStream("bottom left\n".getBytes()), out);
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
        Console ui = new Console(new ByteArrayInputStream("center\n".getBytes()), out);
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
        Console ui = new Console(new ByteArrayInputStream("middle left\nmiddle right\n".getBytes()), out);
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
        Console ui = new Console(new ByteArrayInputStream("bottom right\ny\n".getBytes()), out);
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
        
        assertEquals(game.stage, Stage.newGame);
        assertEquals(game.xWinsCount, 1);
        assertEquals(game.oWinsCount, 0);
    }
}