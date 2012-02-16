package javattt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private UI ui;
    
    public Game() {
        board = new Board();
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
        ui = new Console(this);
        playerX = null;
        playerO = null;

        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        Boolean playVsAi = null;
        while(playVsAi == null) {
            System.out.print("Play vs. AI? y/n ");
            try {
                String answer = in.readLine();
                if(answer.equals("y")) playVsAi = true;
                else if(answer.equals("n")) playVsAi = false;
            }
            catch (Exception e) {
                System.out.println("Error reading answer!");
            }
        }

        if(!playVsAi) {
            playerX = new HumanPlayer(Board.X);
            playerO = new HumanPlayer(Board.O);
        }
        else {
            while(playerX == null) {
                System.out.print("Play as X? y/n ");
                try {
                    String answer = in.readLine();
                    if(answer.equals("y")) {
                        playerX = new HumanPlayer(Board.X);
                        playerO = new AIPlayer(Board.O);
                    }
                    else if(answer.equals("n")) {
                        playerX = new AIPlayer(Board.X);
                        playerO = new HumanPlayer(Board.O);
                    }
                }
                catch (Exception e) {
                    System.out.println("Error reading answer!");
                }
            }
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
        if(winner == Board.X) System.out.println("Player X has won!");
        else if(winner == Board.O) System.out.println("Player O has won!");
        else System.out.println("The game ended in a draw.");
        
        return winner;
    }

    public void start() {
        int xScore = 0;
        int oScore = 0;
        boolean gamePlaying = true;

        do {
            int victor = startOneGame();
            if(victor == Board.X) xScore++;
            else if(victor == Board.O) oScore++;
            
            System.out.println("Player X has won " + xScore + " games and player O has won " + oScore + " games.");
            
            String answer = null;
            while(answer == null || (!answer.equals("y") && !answer.equals("n"))) {
                System.out.print("Play again? y/n ");

                InputStreamReader r = new InputStreamReader(System.in);
                BufferedReader in = new BufferedReader(r);
                try {
                    answer = in.readLine();
                    if(answer.equals("n")) gamePlaying = false;
                }
                catch (Exception e) {
                    System.out.println("Error reading answer!");
                }
            }
        } while(gamePlaying);
    }
    
    public Player otherPlayer(Player player) {
        if(player == playerX) return playerO;
        else return playerX;
    }
    
    public static void main(String[] args) {
        new Game().start();
    }
}
