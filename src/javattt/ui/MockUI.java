package javattt.ui;

import javattt.Board;
import javattt.Side;
import javattt.command.Command;
import javattt.ui.UI;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/13/12
 * Time: 1:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class MockUI implements UI {
    @Override
    public void update(Board board) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Command promptMove(Board board) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Command prompt(String message) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void victoryMessage(String message, int xWinCount, int oWinCount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Command displayMessage(String message) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void displayHelp() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
