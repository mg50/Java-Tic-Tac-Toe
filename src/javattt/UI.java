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
    int[] promptPlayer(Board board);
    boolean prompt(String msg);
    boolean promptPlayAsX();
    boolean promptPlayVsAi();
    boolean promptStartNewGame();
    void victoryMessage(int winner, int xWinCount, int oWinCount);
}
