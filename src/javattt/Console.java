package javattt;

import javattt.command.*;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Console implements UI {

    public static String helpString = "Type in the x-y coordinates of the square you'd like to play, separated by a space.\n" +
            "For example, typing '1 1' attempts to play the top-left square.\n";


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
        for(int y = 0; y < board.size; y++) {
            for(int x = 0; x < board.size; x++) {
                display += getCellSymbol(board, x, y);
                if(x < board.size - 1) display += "|";
                else display += "\n";
            }
            if(y < board.size - 1) {
                for(int i = 0; i < board.size * 2 - 1; i++) display += "-";
                display += "\n";
            }
        }
        
        try {            
            outputStream.write(display + "\n\n");
            outputStream.flush();
        }
        catch(Exception e) {
            System.out.println("Error updating display!");
        }
    }
    
    public Command promptMove(Board board) {
        int[] move = null;

        try {
            outputStream.write("Please enter your next move (or \"help\"): ");
            outputStream.flush();
            String moveString = inputStream.readLine();
            if(moveString.equals("exit")) return new ExitCommand();
            else if (moveString.equals("restart")) return new RestartCommand();

            move = parseMove(moveString);
        }
        catch (Exception e) {
            System.out.println("Error reading move!");
        }

        if(move == null || move[0] < 0 || move[0] >+ board.size || move[1] < 0 || move[1] >= board.size)
            return new InvalidCommand();
        else return new MoveCommand(move);
    }
    
    public Command prompt(String msg) {
        String answer = null;

        try {
            outputStream.write(msg + " y/n ");
            outputStream.flush();
            answer = inputStream.readLine();
            if(answer.equals("exit")) return new ExitCommand();
            else if (answer.equals("restart")) return new RestartCommand();
        }
        catch (Exception e) {
            System.out.println("Error reading/writing prompt!");
        }

        if(answer.equals("y")) return new YesCommand();
        else if(answer.equals("n")) return new NoCommand();
        else return new InvalidCommand();
    }

    public Command promptPlay3x3() {
        return prompt("Play a 3x3 game?");
    }

    public Command promptPlayAsX() {
        return prompt("Play as X?");
    }
    
    public Command promptPlayVsAI() {
        return prompt("Play vs. AI?");
    }
    
    public Command promptStartNewGame() {
        return prompt("Start another game?");
    }
    
    public void victoryMessage(Side winner, int xWinsCount, int oWinsCount) {

        try {
            if(winner != null) outputStream.write("GameStrategy " + getSideSymbol(winner) + " has won!\n");
            else outputStream.write("The game has ended in a draw.\n");
            outputStream.write("GameStrategy X has won " + inflectWinCount(xWinsCount) + " and player O has won " +
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
    
    public Command displayMessage(String message) {
        try {
            outputStream.write(message);
            outputStream.flush();
        }
        catch (Exception e) {
            System.out.println("Error printing message!");
        }

        return new StepCommand();
    }

    public void displayHelp() {
        displayMessage(helpString);
    }
    
    public int[] parseMove(String moveString) {
        int[] move = null;

        if(moveString.equals("help")) displayHelp();

        Pattern p = Pattern.compile("^(\\d+) (\\d+)$");
        Matcher m = p.matcher(moveString);
        if(!m.matches()) return null;
        String xString = m.group(1);
        String yString = m.group(2);

        if(xString == null || yString == null) {

        }
        else {
            int x = Integer.parseInt(xString);
            int y = Integer.parseInt(yString);
            move = new int[] {x - 1, y - 1};
        }

        return move;
    }
}
