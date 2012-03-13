package javattt;

import javattt.command.Command;

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
    public void victoryMessage(Side winner, int xWinCount, int oWinCount) {
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
