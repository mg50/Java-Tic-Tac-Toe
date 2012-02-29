package javattt;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 2/22/12
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Stage {
    newGame,
    receivingPlay3x3,
    receivingPlayVsAI,
    promptingPlayAsX,
    receivingPlayAsX,
    queryingMove,
    receivingMove,
    gameOver,
    promptingStarNewGame,
    receivingStartNewGame,
    halt
}