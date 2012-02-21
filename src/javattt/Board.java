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


    private final static Side[][] BlankGrid = {{Side._, Side._, Side._},
                                               {Side._, Side._, Side._},
                                               {Side._, Side._, Side._}};


    public static Side otherSide(Side side) {
        if(side == Side.X) return Side.O;
        else return Side.X;
    }

    private Side[][] grid;
    
    private Side[][] copyGrid(Side[][] input_grid) {
        Side[][] out = new Side[3][3];
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

    public Side[][] getGrid() {
        return grid;
    }

    public Board(Side[][] input_grid) {
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
        Side[][] cols = new Side[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                cols[i][j] = grid[j][i];
            }
        }
        
        return cols;
    }
    
    public Side[][] diagonals() {
        Side[] left_diag = {getCell(0, 0), getCell(1, 1), getCell(2, 2)};
        Side[] right_diag = {getCell(0, 2), getCell(1, 1), getCell(2, 0)};
        return new Side[][] {left_diag, right_diag};
    }
    
    public Side[][] lines() {
        Side[][] rows = rows();
        Side[][] columns = columns();
        Side[][] diagonals = diagonals();
    
        return new Side[][] {rows[0], rows[1], rows[2], columns[0], columns[1], columns[2], diagonals[0], diagonals[1]};
    }

    public Boolean hasEmptyCell() {
        Side[][] lines = lines();
        for(Side[] line : lines) {
            if(line[0] == Side._ || line[1] == Side._ || line[2] == Side._)
                return true;
        }

        return false;
    }
    
    public ArrayList<int[]> emptyCoords() {
        ArrayList<int[]> list = new ArrayList<int[]>();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(getCell(i, j) == Side._) {
                    list.add(new int[] {i, j});
                }
            }
        }

        return list;
    }


    public Side winner() {
        Side[][] lines = lines();

        Side[] winningXLine = {Side.X, Side.X, Side.X};
        Side[] winningOLine = {Side.O, Side.O, Side.O};
        
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
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
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
