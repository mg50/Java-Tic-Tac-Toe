package javattt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Game {
    
    private Board board;
    private Player playerX;
    private Player playerO;
    public UI ui;

    public Game() {
        board = new Board();
        ui = new Console(this);
    }
    
    public Game(Board input_board) {
        board = input_board;
    }


    public void setPlayerX(Player player) {
        playerX = player;
    }
    
    public void setPlayerO(Player player) {
        playerO = player;
    }
    
    public Board getBoard() {
        return board;
    }

    public int winner() {
        Boolean hasEmptyCell = false;
        int[][] lines = board.lines();

        for(int[] line : lines) {
            if(winnerX(line)) return Board.X;
            else if(winnerO(line)) return Board.O;
        }

        return 0;
    }
    
    public static Boolean winnerX(int[] line) {
        return Arrays.equals(line, new int[] {Board.X, Board.X, Board.X});
    }

    public static Boolean winnerO(int[] line) {
        return Arrays.equals(line, new int[] {Board.O, Board.O, Board.O});
    }
    
    public Boolean isDraw() {
        return (winner() == 0 && !board.hasEmptyCell());
    }

    public void move(int x, int y, Player player) {
        board.setCell(x, y, player.side);
    }

    public int startOneGame() {
        board = new Board();
        playerX = null;
        playerO = null;

        if(ui.prompt("Play vs. AI?")) {
            if(ui.prompt("Play as X?")) {
                playerX = new HumanPlayer(Board.X);
                playerO = new AIPlayer(Board.O);
            }
            else {
                playerX = new AIPlayer(Board.X);
                playerO = new HumanPlayer(Board.O);
            }
        }
        else {
            playerX = new HumanPlayer(Board.X);
            playerO = new HumanPlayer(Board.O);
        }

        Player currentPlayer = playerX;
        int[] moveCoords;
        int winner;
        do {
            ui.update();
            moveCoords = ui.promptPlayer(currentPlayer);
            move(moveCoords[0], moveCoords[1], currentPlayer);
            winner = winner();
            currentPlayer = otherPlayer(currentPlayer);
        } while(winner == 0 && !isDraw());

        ui.update();

        return winner;
    }

    public int[] start() {

        int xScore = 0;
        int oScore = 0;
        int draws = 0;
        boolean gamePlaying = true;

        do {
            board = new Board();
            int victor = startOneGame();
            if(victor == Board.X) xScore++;
            else if(victor == Board.O) oScore++;
            else draws++;
            ui.victoryMessage(victor, xScore, oScore);
            if(!ui.prompt("Play again?")) gamePlaying = false;
        } while(gamePlaying);
        
        return new int[] {xScore, oScore, draws};
    }
    
    public Player otherPlayer(Player player) {
        if(player == playerX) return playerO;
        else return playerX;
    }
    
    public static void main(String[] args) {
        new Game().start();
    }
}
