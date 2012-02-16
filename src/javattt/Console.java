package javattt;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Console implements UI {

    Game game;
    BufferedReader inputStream;
    BufferedWriter outputStream;

    public Console() {
        this(null, System.in, System.out);
    }
    
    public Console(Game game) {
        this(game, System.in, System.out);
    }

    public Console(Game game, InputStream inputStream, OutputStream outputStream) {
        this.game = game;

        InputStreamReader r = new InputStreamReader(inputStream);
        this.inputStream = new BufferedReader(r);
        
        OutputStreamWriter w = new OutputStreamWriter(outputStream);
        this.outputStream = new BufferedWriter(w);
    }


    public void update() {
        String display = "";
        for(int y = 0; y < 3; y++) {
            display += getCellSymbol(0, y) + "|" + getCellSymbol(1, y) + "|" +
                       getCellSymbol(2, y) + "\n";
            if(y < 2) display += "-----\n";
        }
        
        try {            
            outputStream.write(display + "\n\n");
            outputStream.flush();
        }
        catch(Exception e) {
            System.out.println("Error updating display!");
        }
    }
    
    public int[] promptPlayer(Player player) {
        if(player.automated) return player.calculateMove(game.getBoard());

        int[] move = null;

        while(move == null || game.getBoard().getCell(move[0], move[1]) != Board.Empty) {
            try {
                outputStream.write("Please enter your next move: ");
                outputStream.flush();
                String moveString = inputStream.readLine();
                move = parseMove(moveString);
            }
            catch (Exception e) {
                System.out.println("Error reading move!");
            }
        }
        return move;
    }
    
    public boolean prompt(String msg) {
        String answer = null;

        try {
            while(answer == null || (!answer.equals("y") && !answer.equals("n"))) {
                outputStream.write(msg + " y/n ");
                outputStream.flush();
                answer = inputStream.readLine();
            }
        }
        catch (Exception e) {
            System.out.println("Error reading/writing prompt!");
        }

        return answer.equals("y");
    }
    
    public void victoryMessage(int winner, int xWinsCount, int oWinsCount) {

        try {
            if(winner != 0) outputStream.write("Player " + getPlayerSymbol(winner) + " has won!\n");
            else outputStream.write("The game has ended in a draw.\n");
            outputStream.write("Player X has won " + xWinsCount + " games and player O has won " + oWinsCount + " games.\n");
            outputStream.flush();
            
        }
        catch (Exception e) {
            System.out.println("Error writing victory message.");
        }
    }
    
    private String getPlayerSymbol(int side) {
        if(side == Board.X) return "X";
        else if(side == Board.O) return "O";
        else return null;
    }
    private String getCellSymbol(int x, int y) {
        String symbol = getPlayerSymbol(game.getBoard().getCell(x, y));
        int val = game.getBoard().getCell(x, y);
        return symbol == null ? " " : symbol;
    }
    
    public int[] parseMove(String moveString) {
        int[] move;
        if(moveString.equals("top left")) move = new int[] {0, 0};
        else if(moveString.equals("top middle")) move = new int[] {1, 0};
        else if(moveString.equals("top right")) move = new int[] {2, 0};
        else if(moveString.equals("middle left")) move = new int[] {0, 1};
        else if(moveString.equals("center")) move = new int[] {1, 1};
        else if(moveString.equals("middle right")) move = new int[] {2, 1};
        else if(moveString.equals("bottom left")) move = new int[] {0, 2};
        else if(moveString.equals("bottom middle")) move = new int[] {1, 2};
        else if(moveString.equals("bottom right")) move = new int[] {2, 2};
        else move = null;
        return move;
    }
}
