package javattt;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UI {

    void update(Board board);
    int[] promptMove(Board board);
    boolean promptPlayAsX();
    boolean promptPlayVsAi();
    boolean promptStartNewGame();
    void victoryMessage(Side winner, int xWinCount, int oWinCount);
}
