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

    public static final int Infinity = 512;
    public static final int NegInfinity = -1 * Infinity;
    
    public AIPlayer(Side side) {
        super(side);
    }
    
    public int[] determineNextMove(Board board, UI ui) {
        
        ArrayList<int[]> emptyCoords = board.emptyCoords();
        int size = emptyCoords.size();
        if(size == 0) return null;
        
        int[] champion = null;
        int championValue = side == Side.X ? NegInfinity : Infinity;

        for(int i = 0; i < size; i++) {
            int[] emptyCoord = emptyCoords.get(i);
            Board child = board.duplicateBoard();
            child.setCell(emptyCoord[0], emptyCoord[1], side);
            int minimaxValue = scoreChild(child);

            if(side == Side.X && minimaxValue > championValue) {
                championValue = minimaxValue;
                champion = emptyCoord;
            }
            if(side == Side.O && minimaxValue < championValue) {
                championValue = minimaxValue;
                champion = emptyCoord;
            }
        }
        
        return champion;
    }
    
    public int absoluteLeafScore(Board board) {
        Side winner = board.winner();
        if(winner == Side.X) return Infinity;
        else if(winner == Side.O) return NegInfinity;
        else return 0;
    }

    public int scoreChild(Board board) {
        return minimax(board, board.emptyCoords().size(), AIPlayer.NegInfinity, AIPlayer.Infinity, Board.otherSide(side));
    }
    
    public int minimax(Board board, int depth, int alpha, int beta, Side side) {
        int score = absoluteLeafScore(board);
        if(depth == 0 || score != 0) return score;
        Side otherSide = Board.otherSide(side);

        Board[] children = board.childNodes(side);


        for(Board child : children) {
            int childValue = minimax(child, depth - 1, alpha, beta, otherSide);
            if(side == Side.X && childValue > alpha) {
                alpha = childValue;
                if(beta <= alpha) break;
            }
            if(side == Side.O && childValue < beta){
                beta = childValue;
                if(beta <= alpha) break;
            }
        }

        return side == Side.X ? alpha/2 : beta/2 ;
    }
}
