package javattt;

import javattt.command.AlertCommand;
import javattt.command.Command;
import javattt.command.NullCommand;
import javattt.command.VictorCommand;
import javattt.fsm.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static javattt.Side.O;
import static javattt.Side.X;
import static javattt.Side._;

public class LocalGameTest extends TestCase {

    public void testNewGameState() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        assertTrue(game.state instanceof NewGameState);

        game.board = new Board(3);
        game.playerO = game.playerX = game.currentPlayer = new HumanPlayer(Side.X);

        game.state.transition(new NullCommand());
        assertTrue(game.state instanceof PromptingPlay3x3State);
        assertNull(game.playerX);
        assertNull(game.playerO);
        assertNull(game.currentPlayer);
    }

    public void test3x3Prompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(new NullCommand());
        assertTrue(game.state instanceof PromptingPlay3x3State);


        Command cmd = game.state.transition(new NullCommand());
        assertTrue(game.state instanceof ReceivingPlay3x3State);
    }

    public void test3x3PromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("12345\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(new NullCommand());
        assertTrue(game.state instanceof PromptingPlay3x3State);


        Command cmd = game.state.transition(new NullCommand());
        assertTrue(game.state instanceof ReceivingPlay3x3State);
    }

    public void testReceive3x3Prompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        Command cmd = game.state.transition(new NullCommand());
        assertTrue(game.state instanceof ReceivingPlay3x3State);

        game.state.transition(cmd);
        assertTrue(game.state instanceof PromptingPlayVsAIState);
        assertEquals(game.board.size, 3);
    }

    public void testReceive3x3PromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        Command cmd = game.state.transition(new NullCommand());
        assertTrue(game.state instanceof ReceivingPlay3x3State);

        game.state.transition(cmd);
        assertTrue(game.state instanceof PromptingPlayVsAIState);
        assertEquals(game.board.size, 4);
    }

    public void testReceive3x3PromptThree() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("12345\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.state.transition(null);
        Command cmd = game.state.transition(new NullCommand());
        assertTrue(game.state instanceof ReceivingPlay3x3State);

        game.state.transition(cmd);
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
        Command cmd = game.state.transition(new NullCommand());

        game.state.transition(cmd);
        assertTrue(game.state instanceof PromptingPlayAsXState);
    }

    public void testReceivePlayVsAIPromptTwo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("n\n".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingPlayVsAIState(game);
        Command cmd = game.state.transition(new NullCommand());

        game.state.transition(cmd);
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
        Command cmd = game.state.transition(new NullCommand());

        game.state.transition(cmd);
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
        Command cmd = game.state.transition();

        game.state.transition(cmd);
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
        Command cmd = game.state.transition();

        game.state.transition(cmd);
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
        Command cmd = game.state.transition();

        game.state.transition(cmd);
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
        Command cmd = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        game.state.transition(cmd);

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
        Command cmd = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        game.state.transition(cmd);

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
        Command cmd = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        game.state.transition(cmd);

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
        Command cmd = game.state.transition();

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.board.getCell(2, 0), Side._);

        cmd = game.state.transition(cmd);
        cmd = game.state.transition(cmd);
        cmd = game.state.transition(cmd);

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
        Command cmd = game.state.transition();

        assertEquals(game.board.getCell(2, 0), Side._);

        cmd = game.state.transition(cmd);

        assertTrue(game.state instanceof GameOverState);
        assertEquals(game.board.getCell(2, 0), Side.X);

        assertTrue(cmd instanceof VictorCommand);
        assertEquals(((VictorCommand) cmd).side, Side.X);
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
        Command cmd = game.state.transition();
        cmd = game.state.transition(cmd);

        assertEquals(game.xWinsCount, 0);
        assertEquals(game.oWinsCount, 0);

        cmd = game.state.transition(cmd);

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
        Command cmd = game.state.transition();
        game.state.transition(cmd);

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
        Command cmd = game.state.transition();
        game.state.transition(cmd);

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
        Command cmd = game.state.transition();
        game.state.transition(cmd);

        assertTrue(game.state instanceof PromptingStartNewGameState);
    }

    public void testHaltState() throws Exception {
        Game game = new LocalGame(new Console());
        game.state = new HaltState(game);
        game.state.transition();

        assertTrue(game.state instanceof HaltState);
    }

    public void testAlert() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("".getBytes()), out);
        Game game = new LocalGame(ui);
        game.board = new Board();
        game.state = new PromptingStartNewGameState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new HumanPlayer(Side.O);
        game.currentPlayer = game.playerX;
        
        Command cmd = new AlertCommand("test");
        cmd = cmd.sendToGame(game);
        assertTrue(game.state instanceof PromptingAlertState);
                             
        cmd = cmd.sendToGame(game);
        assertTrue(game.state instanceof ReceivingAlertState);
        
        cmd.sendToGame(game);
        assertTrue(game.state instanceof PromptingStartNewGameState);
        
        assertEquals(out.toString(), "test");
    }
    
    public void testPlayGame() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("4 2\n2 3\n3 3\nexit\n".getBytes()), out);
        Game game = new LocalGame(ui);
        Side[][] grid = {{O, _, _, X},
                         {O, _, _, _},
                         {_, _, _, _},
                         {X, _, _, _}};

        game.board = new Board(grid);
        game.state = new PromptingMoveState(game);
        game.playerX = new HumanPlayer(Side.X);
        game.playerO = new AIPlayer(Side.O, game.board.size);
        game.currentPlayer = game.playerX;
        game.start();

        System.out.println(out.toString());
        
        /*
        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        game.state.transition(cmd);

        assertTrue(game.state instanceof PromptingMoveState);
        assertEquals(game.board.getCell(0, 0), Side.O);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.currentPlayer, game.playerX);      */
        
    }
}