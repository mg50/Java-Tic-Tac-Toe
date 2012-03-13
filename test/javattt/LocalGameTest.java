package javattt;

import javattt.command.Command;
import javattt.command.StepCommand;
import javattt.command.VictorCommand;
import javattt.fsm.*;
import javattt.strategy.AIStrategy;
import javattt.strategy.HumanStrategy;
import javattt.ui.Console;
import javattt.ui.MockUI;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static javattt.Side.O;
import static javattt.Side.X;
import static javattt.Side._;

public class LocalGameTest extends TestCase {
    private LocalGame game;

    protected void setUp() {
        game = new LocalGame();
        game.playerX = new Player();
        game.playerO = new Player();
        game.playerX.ui = new MockUI();
        game.playerO.ui = new MockUI();
        game.playerX.side = Side.X;
        game.playerO.side = Side.O;
        game.playerX.gameStrategy = new HumanStrategy();
        game.playerO.gameStrategy = new HumanStrategy();
        game.currentPlayer = game.playerX;
        game.masterPlayer = game.playerX;
        game.board = new Board();
    }
    
    protected void setUI(String s) {
        Console ui = new Console(new ByteArrayInputStream(s.getBytes()), new ByteArrayOutputStream());
        game.playerX.ui = game.playerO.ui = ui;
    }
    
    protected void transition() {
        game.state.readNextCommand().issue(game);
    }

    public void testNewGameState() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\nexit\n".getBytes()), out);
        assertTrue(game.state instanceof NewGameState);
        game.playerX = null;
        game.playerO = null;
        
        new StepCommand().issue(game);
        assertTrue(game.state instanceof PlayVsAIState);
        assertTrue(game.playerX instanceof Player);
        assertTrue(game.playerO instanceof Player);
        assertTrue(game.playerX.ui instanceof MockUI);
        assertTrue(game.playerO.ui instanceof MockUI);
        assertNull(game.currentPlayer);
    }

    public void testPlayVsAIPrompt() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Console ui = new Console(new ByteArrayInputStream("y\n".getBytes()), out);
        setUI("y\n");
        game.state = new PlayVsAIState(game);
        transition();
        assertTrue(game.state instanceof PlayAsXState);
    }
                           
    public void testPlayVsAIPromptTwo() throws Exception {
        game.state = new PlayVsAIState(game);
        setUI("n\n");
        transition();

        assertTrue(game.state instanceof Play3x3State);
        assertTrue(game.playerX.gameStrategy instanceof HumanStrategy);
        assertTrue(game.playerO.gameStrategy instanceof HumanStrategy);
    }

    public void testReceivePlayVsAIPromptThree() throws Exception {
        game.state = new PlayVsAIState(game);
        setUI("asdf\n");
        transition();

        assertTrue(game.state instanceof PlayVsAIState);
    }

    public void testReceivePlayAsXPrompt() throws Exception {
        game.state = new PlayAsXState(game);
        game.versusAI = true;
        setUI("y\n");
        transition();

        assertTrue(game.state instanceof Play3x3State);
        assertEquals(game.masterPlayer, game.playerX);
        assertEquals(game.masterPlayer.side, Side.X);
        assertTrue(game.playerO.gameStrategy instanceof AIStrategy);
    }

    public void testReceivePlayAsXPromptTwo() throws Exception {
        game.state = new PlayAsXState(game);
        game.versusAI = true;
        setUI("n\n");
        transition();

        assertTrue(game.state instanceof Play3x3State);
        assertEquals(game.masterPlayer, game.playerO);
        assertEquals(game.masterPlayer.side, Side.O);
        assertTrue(game.playerX.gameStrategy instanceof AIStrategy);
    }

    public void testReceivePlayAsXPromptThree() throws Exception {
        game.state = new PlayAsXState(game);
        game.versusAI = true;
        setUI("blah\n");
        transition();

        assertTrue(game.state instanceof PlayAsXState);
    }


    public void test3x3State() throws Exception {
        game.state = new Play3x3State(game);
        setUI("y\n");
        game.board = null;
        transition();
        assertTrue(game.state instanceof BeginningGameState);
        assertEquals(game.board.size, 3);
    }

    public void test3x3StateTwo() throws Exception {
        game.state = new Play3x3State(game);
        setUI("n\n");
        game.board = null;
        transition();
        assertTrue(game.state instanceof BeginningGameState);
        assertEquals(game.board.size, 4);
    }

    public void test3x3StateThree() throws Exception {
        game.state = new Play3x3State(game);
        setUI("12345\n");
        game.board = null;
        transition();
        assertTrue(game.state instanceof Play3x3State);
    }
    
    public void testBeginningGame() throws Exception {
        game.state = new BeginningGameState(game);
        game.currentPlayer = null;
        transition();

        
        assertTrue(game.state instanceof MoveState);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testReceiveMoveOne() throws Exception {
        game.state = new MoveState(game);
        game.currentPlayer = game.playerX;
        setUI("1 1\n");

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        transition();

        assertTrue(game.state instanceof MoveState);
        assertEquals(game.board.getCell(0, 0), Side.X);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.currentPlayer, game.playerO);
    }

    public void testReceiveMoveTwo() throws Exception {
        game.state = new MoveState(game);
        game.currentPlayer = game.playerO;
        setUI("1 1\n");

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        transition();

        assertTrue(game.state instanceof MoveState);
        assertEquals(game.board.getCell(0, 0), Side.O);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testReceiveMoveThree() throws Exception {
        game.state = new MoveState(game);
        game.currentPlayer = game.playerX;
        setUI("invalid move\n");

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);

        transition();

        assertTrue(game.state instanceof MoveState);
        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testReceiveMoveFour() throws Exception {
        game.state = new MoveState(game);
        game.currentPlayer = game.playerX;
        setUI("1 1\n2 1\n");

        assertEquals(game.board.getCell(0, 0), Side._);
        assertEquals(game.board.getCell(1, 0), Side._);
        assertEquals(game.board.getCell(2, 0), Side._);

        transition();
        transition();

        assertTrue(game.state instanceof MoveState);
        assertEquals(game.board.getCell(0, 0), Side.X);
        assertEquals(game.board.getCell(1, 0), Side.O);
        assertEquals(game.board.getCell(2, 0), Side._);
        assertEquals(game.currentPlayer, game.playerX);
    }

    public void testReceiveMoveFive() throws Exception {
        Side[][] grid = {{X, X, _}, {_, _, _}, {_, _, _}};
        setUI("3 1\n");
        game.board = new Board(grid);
        game.state = new MoveState(game);

        transition();

        assertTrue(game.state instanceof GameOverState);
        assertEquals(game.board.getCell(2, 0), Side.X);
    }



    public void testGameOver() throws Exception {
        Side[][] grid = {{X, X, _}, {_, _, _}, {_, _, _}};
        setUI("3 1\n");
        game.board = new Board(grid);
        game.state = new MoveState(game);

        transition();
        assertEquals(game.xWinsCount, 0);
        transition();
        assertEquals(game.xWinsCount, 1);
        assertEquals(game.xWinsCount, 1);
        assertTrue(game.state instanceof StartNewGameState);
    }

    public void testGameOverTwo() throws Exception {
        Side[][] grid = {{X, X, _}, {_, _, _}, {_, _, _}};
        setUI("3 1\n");
        game.board = new Board(grid);
        game.state = new MoveState(game);

        transition();
        assertEquals(game.xWinsCount, 0);
        assertEquals(game.oWinsCount, 0);
        transition();
        assertEquals(game.xWinsCount, 1);
        assertEquals(game.oWinsCount, 0);
        assertTrue(game.state instanceof StartNewGameState);
    }

    public void testGameOverThree() throws Exception {
        Side[][] grid = {{O, O, _}, {_, _, _}, {_, _, _}};
        setUI("3 1\n");
        game.board = new Board(grid);
        game.state = new MoveState(game);
        game.currentPlayer = game.playerO;

        transition();
        assertEquals(game.xWinsCount, 0);
        assertEquals(game.oWinsCount, 0);
        transition();
        assertEquals(game.xWinsCount, 0);
        assertEquals(game.oWinsCount, 1);
        assertTrue(game.state instanceof StartNewGameState);
    }

    public void testGameOverFour() throws Exception {
        Side[][] grid = {{O, O, _}, {X, X, O}, {O, X, X}};
        setUI("3 1\n");
        game.board = new Board(grid);
        game.state = new MoveState(game);

        transition();
        assertEquals(game.xWinsCount, 0);
        assertEquals(game.oWinsCount, 0);
        transition();
        assertEquals(game.xWinsCount, 0);
        assertEquals(game.oWinsCount, 0);
        assertTrue(game.state instanceof StartNewGameState);
    }


    public void testStartNewGame() {
        game.state = new StartNewGameState(game);
        setUI("y\n");
        transition();
        
        assertTrue(game.state instanceof NewGameState);
    }

    public void testStartNewGameTwo() {
        game.state = new StartNewGameState(game);
        setUI("n\n");
        transition();

        assertTrue(game.state instanceof HaltState);
    }

    public void testStartNewGameThree() {
        game.state = new StartNewGameState(game);
        setUI("12345\n");
        transition();

        assertTrue(game.state instanceof StartNewGameState);
    }
}