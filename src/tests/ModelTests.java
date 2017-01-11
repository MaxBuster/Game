/**
 * Tests model data and action handling
 * Created by Max Buster
 */

package tests;

import model.Game.Game;
import model.Model;
import model.Player.Player;
import org.junit.Before;
import org.junit.Test;
import tests.Stubs.GameStub;
import utils.Constants.Constants;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ModelTests {
    private Model model;
    private PropertyChangeSupport pcs;

    @Before
    public void setup() {
        ArrayList<Game> games = new ArrayList<Game>();
        for (int i=0; i<5; i++) {
            games.add(new GameStub(i));
        }

        this.pcs = new PropertyChangeSupport(this);
        this.model = new Model(games, pcs);

        for (int i=0; i<5; i++) {
            model.new_player();
        }
    }

    public void set_all_players_to_done() {
        ArrayList<Player> players = model.get_players();
        for (Player player : players) {
            player.set_done(true);
        }
    }

    @Test
    public void test_players_done() {
        set_all_players_to_done();

        assert model.all_players_done();
    }

    @Test
    public void test_players_reset() {
        set_all_players_to_done();
        model.set_players_to_not_done();

        assert !model.all_players_done();
    }

    @Test
    public void test_experiment_start() {
        pcs.firePropertyChange(Constants.START_GAME, null, null);

        assert model.get_current_game_num() == 0;
        assert model.get_current_round_index() == 0;
    }

    @Test
    public void test_round_incrementer() {
        pcs.firePropertyChange(Constants.START_GAME, null, null);
        set_all_players_to_done();
        model.continue_game_if_all_players_done();

        assert model.get_current_round_index() == 1;
    }

    @Test
    public void test_game_incrementer() {
        pcs.firePropertyChange(Constants.START_GAME, null, null);
        for (int i=0; i<Constants.LIST_OF_ROUNDS.length; i++) {
            set_all_players_to_done();
            model.continue_game_if_all_players_done();
        }

        assert model.get_current_game_num() == 1;
    }
}
