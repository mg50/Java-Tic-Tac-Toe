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
    InputStream inputStream;
    OutputStream outputStream;

    public Console() {}

    public Console(Game game) {
        this.game = game;
        this.inputStream = System.in;
        this.outputStream = System.out;
    }

    public Console(Game game, InputStream inputStream, OutputStream outputStream) {
        this.game = game;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void update() {
        String display = "";
        for(int y = 0; y < 3; y++) {
            display += getCellSymbol(0, y) + "|" + getCellSymbol(1, y) + "|" +
                       getCellSymbol(2, y) + "\n";
            if(y < 2) display += "-----\n";
        }
        
        System.out.println(display + "\n\n");
    }
    
    public int[] promptPlayer(Player player) {
        if(player.automated) return player.calculateMove(game.getBoard());

        int[] move = null;
        InputStreamReader r = new InputStreamReader(inputStream);
        BufferedReader in = new BufferedReader(r);

        OutputStreamWriter w = new OutputStreamWriter(outputStream);
        BufferedWriter out = new BufferedWriter(w);

        while(move == null || game.getBoard().getCell(move[0], move[1]) != Board.Empty) {
            try {
                out.write("Please enter your next move: ");
                out.flush();
                move = parseMove(in.readLine());
            }
            catch (Exception e) {
                System.out.println("Error reading move!");
            }
        }
        return move;
    }
    
    private String getCellSymbol(int x, int y) {
        int val = game.getBoard().getCell(x, y);
        if(val == Board.X) return "X";
        else if(val == Board.O) return "O";
        else return " ";
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
