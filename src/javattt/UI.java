package javattt;

import javattt.command.Command;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/14/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UI {

    void update(Board board);
    Command promptMove(Board board);
    Command prompt(String message);
    void victoryMessage(Side winner, int xWinCount, int oWinCount);
    Command displayMessage(String message);
    void displayHelp();
}
