package javattt;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player {

    public int side;
    public Boolean automated;

    public Player(int side) {
        this.side = side;
    }
    
    public int[] calculateMove(Board board) { // No-op
        return null;
    }
    
    public int otherSide() {
        return otherSide(side);
    }
    
    public int otherSide(int side) {
        if(side == Board.X) return Board.O;
        else return Board.X;
    }
}
