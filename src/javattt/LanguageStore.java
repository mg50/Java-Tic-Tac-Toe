package javattt;

/**
 * Created by IntelliJ IDEA.
 * User: MGT
 * Date: 3/13/12
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class LanguageStore {

    public final String PROMPT_PLAY_3x3 = "Play a 3x3 or 4x4 game?";
    public final String PROMPT_PLAY_VS_AI = "Play vs. AI?";
    public final String PROMPT_PLAY_AS_X = "Play as X?";
    public final String PROMPT_START_NEW_GAME = "Start new game?";
    public final String HELP_STRING = "Type in the x-y coordinates of the square you'd like to play, " +
                                      "separated by a space.\nFor example, typing '1 1' attempts to play the " +
                                      "top-left square.\n";
    public final String PLAYER_X_WON = "Player X has won!";
    public final String PLAYER_O_WON = "Player O has won!";
    public final String DRAW = "The game ended in a draw.";


    public static LanguageStore instance = new LanguageStore();

    private LanguageStore() {}
}
