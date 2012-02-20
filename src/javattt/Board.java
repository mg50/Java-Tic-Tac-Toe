package javattt;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Board {

    private final static int[][] BlankGrid = {new int[] {0, 0, 0},
                                        new int[] {0, 0, 0},
                                        new int[] {0, 0, 0}};
    
    public static final int Empty = 0;
    public static final int X = 1;
    public static final int O = 2;

    public static int otherSide(int side) {
        if(side == X) return O;
        else return X;
    }

    private int[][] grid;
    
    private int[][] copyGrid(int[][] input_grid) {
        int[][] out = new int[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                out[i][j] = input_grid[i][j];
            }
        }

        return out;
    }

    public Board() {
        grid = copyGrid(BlankGrid);
    }

    public Board duplicateBoard() {
        return new Board(grid);
    }

    public int[][] getGrid() {
        return grid;
    }

    public Board(int[][] input_grid) {
        grid = copyGrid(input_grid);
    }

    public int getCell(int x, int y) {
        return grid[y][x];
    }
    
    public void setCell(int x, int y, int val) {
        grid[y][x] = val;
    }
    
    public int[][] rows() {
        return grid;
    }
    
    public int[][] columns() {
        int[][] cols = new int[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                cols[i][j] = grid[j][i];
            }
        }
        
        return cols;
    }
    
    public int[][] diagonals() {
        int[] left_diag = {getCell(0, 0), getCell(1, 1), getCell(2, 2)};
        int[] right_diag = {getCell(0, 2), getCell(1, 1), getCell(2, 0)};
        return new int[][] {left_diag, right_diag};
    }
    
    public int[][] lines() {
        int[][] rows = rows();
        int[][] columns = columns();
        int[][] diagonals = diagonals();
    
        return new int[][] {rows[0], rows[1], rows[2], columns[0], columns[1], columns[2], diagonals[0], diagonals[1]};
    }

    public Boolean hasEmptyCell() {
        Boolean hasEmptyCell = false;
        int[][] lines = lines();
        for(int[] line : lines) {
            if(line[0] == Board.Empty || line[1] == Board.Empty || line[2] == Board.Empty)
                return true;
        }

        return false;
    }
    
    public ArrayList<int[]> emptyCoords() {
        ArrayList<int[]> list = new ArrayList<int[]>();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(getCell(i, j) == Board.Empty) {
                    list.add(new int[] {i, j});
                }
            }
        }

        return list;
    }


    public int winner() {
        Boolean hasEmptyCell = false;
        int[][] lines = lines();

        int[] winningXLine = {Board.X, Board.X, Board.X};
        int[] winningOLine = {Board.O, Board.O, Board.O};
        
        for(int[] line : lines) {
            if(Arrays.equals(line, winningXLine)) return Board.X;
            else if(Arrays.equals(line, winningOLine)) return Board.O;
        }

        return 0;
    }


    public Boolean isDraw() {
        return (winner() == 0 && !hasEmptyCell());
    }


    public int hashCode() {
        int hash = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                hash = 10*hash + getCell(j, i);
            }
        }

        return hash;
    }

    @Override
    public boolean equals(Object board) {
        if(!(board instanceof Board)) return false;
        else return Arrays.deepEquals(grid, ((Board) board).getGrid());
    }
    
    public Board[] childNodes(int side) {
        ArrayList<int[]> emptyCoords = emptyCoords();
        int size = emptyCoords.size();
        Board[] children = new Board[size];
        for(int i = 0; i < size; i++) {
            int[] coords = emptyCoords.get(i);
            children[i] = duplicateBoard();
            children[i].setCell(coords[0], coords[1], side);
        }

        return children;
    }
}
