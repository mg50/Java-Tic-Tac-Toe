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


    public static Side[][] blankGrid(int size) {
        Side[][] blank = new Side[size][size];
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                blank[x][y] = Side._;
            }
        }

        return blank;
    }

    public static final int DEFAULT_SIZE = 3;

    public int size;

    public static Side otherSide(Side side) {
        if(side == Side.X) return Side.O;
        else return Side.X;
    }

    private Side[][] grid;
    
    private static Side[][] copyGrid(Side[][] input_grid) {
        int size = input_grid.length;
        Side[][] out = new Side[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                out[i][j] = input_grid[i][j];
            }
        }

        return out;
    }

    public Board() {
        this(DEFAULT_SIZE);
    }
    
    public Board(int size) {
        this.size = size;
        grid = copyGrid(blankGrid(size));
    }

    public Board duplicateBoard() {
        return new Board(grid);
    }

    public Side[][] getGrid() {
        return grid;
    }

    public Board(Side[][] input_grid) {
        size = input_grid.length;
        grid = copyGrid(input_grid);
    }

    public Side getCell(int x, int y) {
        return grid[y][x];
    }
    
    public void setCell(int x, int y, Side val) {
        grid[y][x] = val;
    }
    
    public Side[][] rows() {
        return grid;
    }
    
    public Side[][] columns() {
        Side[][] cols = new Side[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                cols[i][j] = grid[j][i];
            }
        }
        
        return cols;
    }
    
    public Side[][] diagonals() {
        Side[] left_diag = new Side[size];
        Side[] right_diag = new Side[size];
        
        for(int i = 0; i < size; i++) {
            left_diag[i] = getCell(i, i);
            right_diag[i] = getCell(size - i - 1, i);
        }

        return new Side[][] {left_diag, right_diag};
    }
    
    public Side[][] lines() {
        Side[][] rows = rows();
        Side[][] columns = columns();
        Side[][] diagonals = diagonals();
        
        ArrayList<Side[]> lines = new ArrayList<Side[]>();
        for(Side[] row : rows) lines.add(row);
        for(Side[] column : columns) lines.add(column);
        for(Side[] diag : diagonals) lines.add(diag);
        
        Side[][] ret = new Side[lines.size()][size];
        int numLines = lines.size();
        for(int i = 0; i < numLines; i++) ret[i] = lines.get(i);

        return ret;
    }

    public Boolean hasEmptyCell() {
        Side[][] lines = lines();
        for(Side[] line : lines) {
            for(int i = 0; i < line.length; i++) {
                if(line[i] == Side._) return true;
            }
        }

        return false;
    }
    
    public ArrayList<int[]> emptyCoords() {
        ArrayList<int[]> list = new ArrayList<int[]>();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(getCell(i, j) == Side._) {
                    list.add(new int[] {i, j});
                }
            }
        }

        return list;
    }


    public Side winner() {
        Side[][] lines = lines();

        Side[] winningXLine = new Side[size];
        Side[] winningOLine = new Side[size];
        for(int i = 0; i < size; i++) {
            winningXLine[i] = Side.X;
            winningOLine[i] = Side.O;
        }

        for(Side[] line : lines) {
            if(Arrays.equals(line, winningXLine)) return Side.X;
            else if(Arrays.equals(line, winningOLine)) return Side.O;
        }

        return null;
    }

    public Boolean isDraw() {
        return (winner() == null && !hasEmptyCell());
    }


    public int sideValue(Side s) {
        if(s == Side.X) return 1;
        else if(s == Side.O) return 2;
        else return 0;
    }
    
    public int hashCode() {
        int hash = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                hash = 10*hash + sideValue(getCell(j, i));
            }
        }

        return hash;
    }

    @Override
    public boolean equals(Object board) {
        if(!(board instanceof Board)) return false;
        else return Arrays.deepEquals(grid, ((Board) board).getGrid());
    }
    
    public Board[] childNodes(Side side) {
        ArrayList<int[]> emptyCoords = emptyCoords();
        int numEmptyCoords = emptyCoords.size();
        Board[] children = new Board[numEmptyCoords];
        for(int i = 0; i < numEmptyCoords; i++) {
            int[] coords = emptyCoords.get(i);
            children[i] = duplicateBoard();
            children[i].setCell(coords[0], coords[1], side);
        }

        return children;
    }
}
