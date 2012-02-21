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

    public Game(UI ui) {
        board = new Board();
        this.ui = ui;
    }
    public Game(Board input_board) {
        board = input_board;
    }

    public Game(Board input_board, UI ui) {
        board = input_board;
        this.ui = ui;
    }

    public Board getBoard() {
        return board;
    }


    public void move(int x, int y, Player player) {
        board.setCell(x, y, player.side);
    }

    public Side startOneGame() {
        board = new Board();
        playerX = null;
        playerO = null;

        if(ui.promptPlayVsAi()) {
            if(ui.promptPlayAsX()) {
                playerX = new HumanPlayer(Side.X);
                playerO = new AIPlayer(Side.O);
            }
            else {
                playerX = new AIPlayer(Side.X);
                playerO = new HumanPlayer(Side.O);
            }
        }
        else {
            playerX = new HumanPlayer(Side.X);
            playerO = new HumanPlayer(Side.O);
        }

        Player currentPlayer = playerX;
        int[] moveCoords;
        Side winner = null;
        do {
            ui.update(board);
            if(currentPlayer.automated) moveCoords = currentPlayer.calculateMove(board);
            else moveCoords = ui.promptPlayer(board);

            move(moveCoords[0], moveCoords[1], currentPlayer);
            winner = board.winner();
            if(currentPlayer == playerX) currentPlayer = playerO;
            else currentPlayer = playerX;
        } while(winner == null && !board.isDraw());

        ui.update(board);

        return winner;
    }

    public int[] start() {

        this.ui = ui;
        int xScore = 0;
        int oScore = 0;
        int draws = 0;
        boolean gamePlaying = true;

        do {
            board = new Board();
            Side victor = startOneGame();
            if(victor == Side.X) xScore++;
            else if(victor == Side.O) oScore++;
            else draws++;
            ui.victoryMessage(victor, xScore, oScore);
            if(!ui.promptStartNewGame()) gamePlaying = false;
        } while(gamePlaying);
        
        return new int[] {xScore, oScore, draws};
    }

    public static void main(String[] args) {
    }
}
