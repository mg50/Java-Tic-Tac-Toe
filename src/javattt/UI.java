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
    TransitionData promptMove(Board board);
    TransitionData promptPlay3x3();
    TransitionData promptPlayAsX();
    TransitionData promptPlayVsAI();
    TransitionData promptStartNewGame();
    void victoryMessage(Side winner, int xWinCount, int oWinCount);
    void displayHelp();
}
