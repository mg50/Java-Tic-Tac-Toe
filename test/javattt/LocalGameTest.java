package javattt;

import javattt.fsm.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static javattt.Side.X;
import static javattt.Side.O;
import static javattt.Side._;

public class LocalGameTest extends TestCase {

    public void testNewGameState() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        assertTrue(game.state instanceof NewGameState);

        game.board = new Board(3);
        game.playerO = game.playerX = game.currentPlayer = new HumanPlayer(Side.X);

        TransitionData data = game.state.transition(null);
        assertTrue(game.state instanceof PromptingPlay3x3State);
        assertNull(game.board);
        assertNull(game.playerX);
        assertNull(game.playerO);
        assertNull(game.currentPlayer);

    }

    public void test3x3Prompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        assertTrue(game.state instanceof PromptingPlay3x3State);


        TransitionData data = game.state.transition(null);
        assertTrue(game.state instanceof ReceivingPlay3x3State);
    }

    public void test3x3PromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("12345\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        assertTrue(game.state instanceof PromptingPlay3x3State);


        TransitionData data = game.state.transition(null);
        assertTrue(game.state instanceof ReceivingPlay3x3State);
    }

    public void testReceive3x3Prompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        TransitionData data = game.state.transition(null);
        assertTrue(game.state instanceof ReceivingPlay3x3State);

        game.state.transition(data);
        assertTrue(game.state instanceof PromptingPlayVsAIState);
        assertEquals(game.board.size, 3);
    }

    public void testReceive3x3PromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        TransitionData data = game.state.transition(null);
        assertTrue(game.state instanceof ReceivingPlay3x3State);

        game.state.transition(data);
        assertTrue(game.state instanceof PromptingPlayVsAIState);
        assertEquals(game.board.size, 4);
    }

    public void testReceive3x3PromptThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("12345\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        TransitionData data = game.state.transition(null);
        assertTrue(game.state instanceof ReceivingPlay3x3State);

        game.state.transition(data);
        assertTrue(game.state instanceof PromptingPlay3x3State);
    }

    public void testPromptPlayVsAI() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayVsAIState(game);
        game.state.transition(null);
        assertTrue(game.state instanceof ReceivingPlayVsAIState);
    }

    public void testReceivePlayVsAIPrompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayVsAIState(game);
        TransitionData data = game.state.transition(null);

        game.state.transition(data);
        assertTrue(game.state instanceof PromptingPlayAsXState);
    }

    public void testReceivePlayVsAIPromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayVsAIState(game);
        TransitionData data = game.state.transition(null);

        game.state.transition(data);
        assertTrue(game.state instanceof BeginningGameState);
        assertTrue(game.playerX instanceof HumanPlayer);
        assertTrue(game.playerO instanceof HumanPlayer);
    }

    public void testReceivePlayVsAIPromptThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("asdf\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayVsAIState(game);
        TransitionData data = game.state.transition(null);

        game.state.transition(data);
        assertTrue(game.state instanceof PromptingPlayVsAIState);
    }

    public void testPromptPlayAsX() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayAsXState(game);
        game.state.transition(null);

        assertTrue(game.state instanceof ReceivingPlayAsXState);

    }

    public void testReceivePlayAsXPrompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayAsXState(game);
        TransitionData data = game.state.transition();

        game.state.transition(data);
        assertTrue(game.state instanceof BeginningGameState);
        assertTrue(game.playerX instanceof HumanPlayer);
        assertTrue(game.playerO instanceof AIPlayer);
    }

    public void testReceivePlayAsXPromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayAsXState(game);
        TransitionData data = game.state.transition();

        game.state.transition(data);
        assertTrue(game.state instanceof BeginningGameState);
        assertTrue(game.playerX instanceof AIPlayer);
        assertTrue(game.playerO instanceof HumanPlayer);
    }

    public void testReceivePlayAsXPromptThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("t\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayAsXState(game);
        TransitionData data = game.state.transition();

        game.state.transition(data);
        assertTrue(game.state instanceof PromptingPlayAsXState);
    }

    public void testBeginningGame() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("t\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new BeginningGameState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = null;
        game.state.transition();

        assertTrue(game.state instanceof PromptingMoveState);
        assertEquals(game.playerX, game.currentPlayer);
    }

    public void testPromptMove() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("1 1\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingMoveState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        game.state.transition();

        assertTrue(game.state instanceof ReceivingMoveState);

    }

    public void testReceiveMoveOne() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("1 1\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingMoveState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        game.state.transition(data);

        assertTrue(game.state instanceof PromptingMoveState);
        assertEquals(game.board.getCell(0, 0), Side.X);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.currentPlayer, game.playerO);
    }

    public void testReceiveMoveTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("1 1\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingMoveState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerO;
        TransitionData data = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        game.state.transition(data);

        assertTrue(game.state instanceof PromptingMoveState);
        assertEquals(game.board.getCell(0, 0), Side.O);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testReceiveMoveThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("invalid move\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingMoveState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        game.state.transition(data);

        assertTrue(game.state instanceof PromptingMoveState);
        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testReceiveMoveFour() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("2 1\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingMoveState(game);
        game.playerX = new AIPlayer(Side.X, game.board.size);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.board.getCell(2, 0), Side._);

        data = game.state.transition(data);
        data = game.state.transition(data);
        data = game.state.transition(data);

        assertTrue(game.state instanceof PromptingMoveState);
        assertEquals(game.board.getCell(0, 0), Side.X);
        assertEquals(game.board.getCell(1, 0), Side.O);
        assertEquals(game.board.getCell(2, 0), Side._);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testReceiveMoveFive() throws Exception {
        Side[][] grid = {{X, X, _}, {_, _, _}, {_, _, _}};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("3 1\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board(grid);
        game.state = new PromptingMoveState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();

        assertEquals(game.board.getCell(2, 0), Side._);

        data = game.state.transition(data);

        assertTrue(game.state instanceof GameOverState);
        assertEquals(game.board.getCell(2, 0), Side.X);
        assertEquals(data.signal, TransitionData.Signal.VICTOR);
        assertEquals(data.side, Side.X);
    }

    public void testGameOver() throws Exception {
        Side[][] grid = {{X, X, _}, {_, _, _}, {_, _, _}};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("3 1\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board(grid);
        game.state = new PromptingMoveState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();
        data = game.state.transition(data);

        assertEquals(game.xWinsCount, 0);
        assertEquals(game.oWinsCount, 0);

        data = game.state.transition(data);

        assertTrue(game.state instanceof PromptingStartNewGameState);
        assertEquals(game.xWinsCount, 1);
        assertEquals(game.oWinsCount, 0);
    }

    public void testPromptNewGame() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingStartNewGameState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        game.state.transition();

        assertTrue(game.state instanceof ReceivingStartNewGameState);
    }

    public void testReceiveNewGame() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingStartNewGameState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();
        game.state.transition(data);

        assertTrue(game.state instanceof NewGameState);
    }

    public void testReceiveNewGameTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingStartNewGameState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();
        game.state.transition(data);

        assertTrue(game.state instanceof HaltState);
    }


    public void testReceiveNewGameThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("444\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingStartNewGameState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        TransitionData data = game.state.transition();
        game.state.transition(data);

        assertTrue(game.state instanceof PromptingStartNewGameState);
    }

    public void testHaltState() throws Exception {
        Game game = new LocalGame(new Console());
        game.state = new HaltState(game);
        game.state.transition();

        assertTrue(game.state instanceof HaltState);
    }


    //public testin
          /*
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
    }   */
}