package javattt;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/15/12
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class AIPlayer extends Player {

    public static final int Infinity = 1;
    public static final int NegInfinity = -1;
    
    public AIPlayer(int side) {
        super(side);
        automated = true;
    }
    
    public int[] calculateMove(Board board) {
        
        //System.out.println("\n\n\n");
        ArrayList<int[]> emptyCoords = board.emptyCoords();
        int size = emptyCoords.size();
        if(size == 0) return null;
        
        int[] champion = null;
        float championValue = side == Board.X ? -1 : 1;

        for(int i = 0; i < size; i++) {
            int[] emptyCoord = emptyCoords.get(i);
            Board child = board.duplicateBoard();
            child.setCell(emptyCoord[0], emptyCoord[1], side);
            float minimaxValue = scoreChild(child);

            //System.out.println("Player " + side + " [" + emptyCoord[0] + ", " + emptyCoord[1] + "]: " + minimaxValue);

            if(side == Board.X && minimaxValue > championValue) {
                championValue = minimaxValue;
                champion = emptyCoord;
            }
            if(side == Board.O && minimaxValue < championValue) {
                championValue = minimaxValue;
                champion = emptyCoord;
            }
        }
        
        return champion;
    }
    
    public int absoluteLeafScore(Board board) {
        Game game = new Game(board);
        int winner = game.winner();
        if(winner == Board.X) return 1;
        else if(winner == Board.O) return -1;
        else return 0;
    }

    public float scoreChild(Board board) {
        return minimax(board, board.emptyCoords().size(), AIPlayer.NegInfinity, AIPlayer.Infinity, otherSide());
    }
    
    public float minimax(Board board, int depth, float alpha, float beta, int side) {
        int score = absoluteLeafScore(board);
        if(depth == 0 || score != 0) return score;
        int otherSide = otherSide(side);

        Board[] children = board.childNodes(side);


        for(Board child : children) {
            float childValue = minimax(child, depth - 1, alpha, beta, otherSide);
            if(side == Board.X && childValue > alpha) {
                alpha = childValue;
                if(beta <= alpha) break;
            }
            if(side == Board.O && childValue < beta){
                if(childValue == -2) childValue = -1;
                beta = childValue;
                if(beta <= alpha) break;
            }
        }

        return side == Board.X ? alpha/(float) depth : beta/(float) depth ;
    }
}
