package javattt.strategy;

import javattt.Board;
import javattt.Side;
import javattt.ui.UI;
import javattt.command.Command;
import javattt.command.MoveCommand;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/15/12
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class AIStrategy extends GameStrategy {

    public static HashMap<Integer, Integer> gameHashForX = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> gameHashForO = new HashMap<Integer, Integer>();

    public int infinity;
    public int negInfinity;
    public AIStrategy(int boardSize) {
        super();
        infinity = (int) Math.pow(2, boardSize*boardSize);
        negInfinity = -1 * infinity;
    }
    
    public Command determineNextMove(Side side, Board board, UI ui) {
        
        ArrayList<int[]> emptyCoords = board.emptyCoords();
        int size = emptyCoords.size();
        if(size == 0) return null;
        
        int[] champion = null;
        int championValue = side == Side.X ? negInfinity : infinity;

        for(int i = 0; i < size; i++) {
            int[] emptyCoord = emptyCoords.get(i);
            Board child = board.duplicateBoard();
            child.setCell(emptyCoord[0], emptyCoord[1], side);
            int minimaxValue = scoreChild(side, child);

            //System.out.println("[" + emptyCoord[0] + ", " + emptyCoord[1] + "]: " + minimaxValue);
            
            if(side == Side.X && minimaxValue > championValue) {
                championValue = minimaxValue;
                champion = emptyCoord;
            }
            if(side == Side.O && minimaxValue < championValue) {
                championValue = minimaxValue;
                champion = emptyCoord;
            }
        }

        return new MoveCommand(champion);
    }
    
    public int absoluteLeafScore(Board board) {
        Side winner = board.winner();
        if(winner == Side.X) return infinity;
        else if(winner == Side.O) return negInfinity;
        else return 0;
    }

    public int scoreChild(Side side, Board board) {
        return minimax(board, board.emptyCoords().size(), negInfinity, infinity, Board.otherSide(side));
    }
    
    public int minimax(Board board, int depth, int alpha, int beta, Side side) {
        int boardHash = board.hashCode();
        if(side == Side.X && gameHashForX.get(new Integer(boardHash)) != null) {
            return gameHashForX.get(new Integer(boardHash));
        }
        
        else if(side == Side.O && gameHashForO.get(new Integer(boardHash)) != null) {
            return gameHashForO.get(new Integer(boardHash));
        }

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

        int ret;
        if(side == Side.X) {
            ret = alpha;
            gameHashForX.put(new Integer(boardHash), new Integer(ret));
        }
        else {
            ret = beta;
            gameHashForO.put(new Integer(boardHash), new Integer(ret));
        }

        return ret;
    }
}
