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

    BufferedReader inputStream;
    BufferedWriter outputStream;

    public Console() {
        this(System.in, System.out);
    }

    public Console(InputStream inputStream, OutputStream outputStream) {
        InputStreamReader r = new InputStreamReader(inputStream);
        this.inputStream = new BufferedReader(r);
        
        OutputStreamWriter w = new OutputStreamWriter(outputStream);
        this.outputStream = new BufferedWriter(w);
    }


    public void update(Board board) {
        String display = "";
        for(int y = 0; y < 3; y++) {
            display += getCellSymbol(board, 0, y) + "|" + getCellSymbol(board, 1, y) + "|" +
                       getCellSymbol(board, 2, y) + "\n";
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
    
    public TransitionData promptMove(Board board) {
        int[] move = null;

        try {
            outputStream.write("Please enter your next move (or \"help\"): ");
            outputStream.flush();
            String moveString = inputStream.readLine();
            if(moveString.equals("exit")) return new TransitionData(TransitionData.Signal.EXIT);
            move = parseMove(moveString);
        }
        catch (Exception e) {
            System.out.println("Error reading move!");
        }

        if(move != null) return new TransitionData(move);
        else return new TransitionData(TransitionData.Signal.INVALID);
    }
    
    public TransitionData prompt(String msg) {
        String answer = null;

        try {
            outputStream.write(msg + " y/n ");
            outputStream.flush();
            answer = inputStream.readLine();
            if(answer.equals("exit")) return new TransitionData(TransitionData.Signal.EXIT);
        }
        catch (Exception e) {
            System.out.println("Error reading/writing prompt!");
        }

        if(answer.equals("y")) return new TransitionData(true);
        else if(answer.equals("n")) return new TransitionData(false);
        else return new TransitionData(TransitionData.Signal.INVALID);
    }
    
    public TransitionData promptPlayAsX() {
        return prompt("Play as X?");
    }
    
    public TransitionData promptPlayVsAI() {
        return prompt("Play vs. AI?");
    }
    
    public TransitionData promptStartNewGame() {
        return prompt("Start another game?");
    }
    
    public void victoryMessage(Side winner, int xWinsCount, int oWinsCount) {

        try {
            if(winner != null) outputStream.write("Player " + getSideSymbol(winner) + " has won!\n");
            else outputStream.write("The game has ended in a draw.\n");
            outputStream.write("Player X has won " + inflectWinCount(xWinsCount) + " and player O has won " +
                               inflectWinCount(oWinsCount) + ".\n");
            outputStream.flush();
            
        }
        catch (Exception e) {
            System.out.println("Error writing victory message.");
        }
    }
    
    public static String inflectWinCount(int count) {
        if(count == 1) return count + " game";
        else return count + " games";
    }
    
    private String getSideSymbol(Side side) {
        if(side == Side.X) return "X";
        else if(side == Side.O) return "O";
        else return " ";
    }
    private String getCellSymbol(Board board, int x, int y) {
        return getSideSymbol(board.getCell(x, y));
    }
    
    public int[] parseMove(String moveString) {
        int[] move;
        
        if(moveString.equals("help")) {
            try {
                String helpString = "Type one of the following to make a move: top left, top middle, top right, " +
                                    "middle left, center, middle right, bottom left, bottom middle, bottom right.\n";
                outputStream.write(helpString);
                outputStream.flush();
            }
            catch (Exception e) {
                System.out.println("Error printing help message!");
            }
        }
        
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
