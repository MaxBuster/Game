/**
 * Created by Max Buster
 */

package tests;

import model.Game;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.FileIO.ConfigReader;

import java.io.IOException;
import java.util.ArrayList;

public class ConfigReaderTests {
    private String file_name;

    @Before
    public void set_up() {
        this.file_name = "test_resources/test_config.json";
    }

    @After
    public void tear_down() {

    }

    @Test
    public void test_json_read_parse_and_decode() throws IOException, JSONException {
        /* Test read and parse */
        String file_contents = ConfigReader.get_file_contents(file_name);
        JSONObject json_contents = ConfigReader.convert_string_to_json(file_contents);

        /* Test json decode worked correctly */
        assert json_contents.getInt("payoff_multiplier") == 4;
        assert json_contents.getJSONArray("games").getJSONObject(0).getInt("budget") == 3;
    }

    @Test
    public void test_game_creator() throws IOException, JSONException {
        ArrayList<Game> games = ConfigReader.get_games_from_config(file_name);
        assert games.size() == 1;
    }
}
