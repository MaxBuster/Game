package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import model.Candidate;
import model.Distribution;
import model.Game;

/**
 * Reads a config file in and convert the info into game objects
 * @author Max Buster
 */

public class ConfigReader {
	private int payoff_intercept;
	private int payoff_multiplier;
	private ArrayList<Game> games;
	
	public ConfigReader() {
		this.games = new ArrayList<Game>();
	}
	
	/**
	 * FIXME handle extra commas and spaces and stuff
	 */

	/**
	 * Read a csv file as game objects
	 */
	public boolean read_config(String file_name) {
		File file = new File(file_name);
		if (file.canRead()) {
			try {
				FileReader fReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fReader);
				read_multipliers(reader); 
				
				String line;
				// Read each game into a game object
				while ((line = reader.readLine()) != null && !line.isEmpty()) { 
					Game game = read_game(reader);
					games.add(game);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
		}
		return true;
	}
	
	public ArrayList<Game> get_games() {
		return this.games;
	}
	
	public int get_payoff_intercept() {
		return this.payoff_intercept;
	}
	
	public int get_payoff_multiplier() {
		return this.payoff_multiplier;
	}
	
	/**
	 * Read the multiplier and intercept that manipulate payoffs
	 */
	private void read_multipliers(BufferedReader reader) throws Exception {
		reader.readLine(); // TODO check it is a comment
		String payoff_intercept_string = reader.readLine();
		String payoff_multiplier_string = reader.readLine();
		
		payoff_intercept = Integer.parseInt(payoff_intercept_string);
		payoff_multiplier = Integer.parseInt(payoff_multiplier_string);
	}
	
	/**
	 * Read and parse lines into game info
	 * @throws Exception if reading or parsing goes wrong
	 */
	private Game read_game(BufferedReader reader) throws Exception {
		String candidate_points = reader.readLine();
		String candidate_parties = reader.readLine();
		String game_distribution = reader.readLine();
		String game_budget = reader.readLine();
		
		int[] ideal_points = parse_ints(split_string(candidate_points));
		int[] parties = parse_ints(split_string(candidate_parties));
		int[] distribution = parse_ints(split_string(game_distribution));
		int budget = Integer.parseInt(game_budget);
		
		HashMap<Integer, Candidate> candidates = create_candidates(ideal_points, parties);
		Game game = new Game(games.size(), candidates, new Distribution(distribution), budget);
		return game;
	}
	
	/**
	 * Create candidate objects given ideal points and parties from the config
	 */
	private HashMap<Integer, Candidate> create_candidates(int[] ideal_points, int[] parties) {
		HashMap<Integer, Candidate> candidates = new HashMap<Integer, Candidate>();
		for (int i=0; i<ideal_points.length; i++) {
			Candidate candidate = new Candidate(i, parties[i], ideal_points[i]);
			candidates.put(i, candidate);
		}
		return candidates;
	}
	
	/**
	 * Convert strings into ints
	 */
	private int[] parse_ints(String[] split_string) {
		int[] parsed_ints = new int[split_string.length];
		for (int i=0; i<split_string.length; i++) {
			parsed_ints[i] = Integer.parseInt(split_string[i]);
		}
		return parsed_ints;
	}
	
	/**
	 * Split a csv string into an array of strings
	 */
	private String[] split_string(String s) {
		s = s.replaceAll("\\s", "");
		s = s.replaceAll(",", " ");
		String[] s_split = s.split("\\s");
		return s_split;
	}
}
