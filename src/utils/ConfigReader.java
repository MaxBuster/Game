/**
 * Reads a json formatted config file in and convert the info into game objects
 * @author Max Buster
 */

package utils;

import java.io.*;
import java.util.ArrayList;

import model.Candidate;
import model.Distributions.BiasDistribution;
import model.Distributions.VoterDistribution;
import org.json.*;

import model.Game;

public class ConfigReader {

	/**
	 * @param file_name - the config file in the same directory as the program
	 * @return a list of game objects that are configured according to the input file
	 * @throws IOException if there is an error finding or reading from the file
	 * @throws JSONException if the file is not correctly json formatted
	 */
	public static ArrayList<Game> get_games_from_config(String file_name) throws IOException, JSONException {
		ArrayList<Game> all_games = new ArrayList<>();

		String file_contents = get_file_contents(file_name);
		JSONObject json_contents = convert_string_to_json(file_contents);

		int games_payoff_multiplier = json_contents.getInt("payoff_multiplier");
		int games_payoff_max = json_contents.getInt("payoff_max");

		JSONArray games = json_contents.getJSONArray("games");
		for (int game_num=0; game_num<games.length(); game_num++) {
			JSONObject json_game = games.getJSONObject(game_num);
			Game game = get_game_from_json(game_num, json_game);
			game.with_payoff_multiplier(games_payoff_multiplier).with_payoff_max(games_payoff_max);
			all_games.add(game);
		}

		return all_games;
	}

	/**
	 * @param json_game - a single game's configuration json formatted
	 * @return a Game object containing the information from the config
	 * @throws JSONException if json cannot be parsed into objects
	 */
	public static Game get_game_from_json(int game_num, JSONObject json_game) throws JSONException {
		JSONArray candidate_json_info = json_game.getJSONArray("candidate_positions");
		ArrayList<Candidate> candidates = get_candidates_from_json(candidate_json_info);

		JSONObject json_voter_dist = json_game.getJSONObject("voter_distribution");
		VoterDistribution voter_distribution = get_voter_dist_from_json(json_voter_dist);

		int budget = json_game.getInt("budget");

		JSONObject json_bias_dist = json_game.getJSONObject("bias_distribution");
		BiasDistribution bias_distribution = get_bias_dist_from_json(json_bias_dist);

		return new Game(game_num, candidates, voter_distribution, bias_distribution, budget);
	}

	/**
	 * @param candidate_list - a json array containing candidate positions
	 * @return an ArrayList of candidate objects with positions from config
	 * @throws JSONException if json read fails
	 */
	public static ArrayList<Candidate> get_candidates_from_json(JSONArray candidate_list) throws JSONException {
		ArrayList<Candidate> candidates = new ArrayList<>();
		for (int candidate_num=0; candidate_num<candidate_list.length(); candidate_num++) {
			int candidate_position = candidate_list.getInt(candidate_num);
			candidates.add(new Candidate(candidate_num, candidate_position));
		}
		return candidates;
	}

	/**
	 * @param distribution_object - json object containing voter distribution info
	 * @return generated VoterDistribution object from the config
	 * @throws JSONException on malformed json object
	 */
	public static VoterDistribution get_voter_dist_from_json(JSONObject distribution_object) throws JSONException {
		int voter_std_dev_1 = distribution_object.getInt("std_dev_1");
		int voter_mean_1 = distribution_object.getInt("mean_1");
		int voter_std_dev_2 = distribution_object.getInt("std_dev_2");
		int voter_mean_2 = distribution_object.getInt("mean_2");
		return new VoterDistribution(voter_std_dev_1, voter_mean_1, voter_std_dev_2, voter_mean_2);
	}

	/**
	 * @param bias_distribution - json object containing candidate bias distribution info
	 * @return generated BiasDistribution object from the config
	 * @throws JSONException on malformed json object
	 */
	public static BiasDistribution get_bias_dist_from_json(JSONObject bias_distribution) throws JSONException {
		int bias_std_dev = bias_distribution.getInt("std_dev");
		int bias_mean = bias_distribution.getInt("mean");
		return new BiasDistribution(bias_std_dev, bias_mean);
	}

	/**
	 * @param file_name - The path to the text file to read from
	 * @return A string containing the contents of the file
	 * @throws IOException - On file read error
	 */
	public static String get_file_contents(String file_name) throws IOException {
		InputStream file_input = new FileInputStream(file_name);
		BufferedReader buff_reader = new BufferedReader(new InputStreamReader(file_input));

		String line = buff_reader.readLine();
		StringBuilder file_contents_builder = new StringBuilder();

		while(line != null) {
			file_contents_builder.append(line);
			line = buff_reader.readLine();
		}

		return file_contents_builder.toString();
	}

	/**
	 * @param json_string - json formatted string
	 * @return a JSON object formatted from the json string
	 * @throws JSONException - if input string is not correctly formatted json
	 */
	public static JSONObject convert_string_to_json(String json_string) throws JSONException {
		return new JSONObject(json_string);
	}
}
