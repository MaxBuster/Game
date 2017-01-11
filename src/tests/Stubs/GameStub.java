/**
 * Allows testing game without side effects
 * Created by Max Buster
 */

package tests.Stubs;

import model.Game.Game;

public class GameStub extends Game {
    public GameStub(int game_num) {
        super(game_num, null, null, null, 0);
    }
}
