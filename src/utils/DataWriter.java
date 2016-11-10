package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Game;
import model.Model;
import model.Player;

public class DataWriter {
	
	public static void write_data(String file_name, Model model) {
		File file = new File(file_name);
		if (file.canWrite()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				Game[] games = model.get_all_games();
				ArrayList<Player> players = model.get_players();
				for (int i=0; i<games.length; i++) {
					Game current_game = games[i];
					write_game_info(current_game, writer);
					write_break(writer);
					for (Player p : players) {
						write_player_info_for_game(p, current_game.getGameNumber(), writer);
					}
					write_break(writer);
				}
			} catch (IOException e) {
				// FIXME return false?
			}
		}
	}
	
	public static void write_break(BufferedWriter writer) {
		try {
			writer.write("\n");
		} catch (IOException e) {
			// FIXME react
		}
	}
	
	public static void write_game_info(Game game, BufferedWriter writer) {
		try {
			writer.write("// Game: " + game.getGameNumber());
			write_break(writer);
			writer.write("// Budget: " + game.getBudget());
			write_break(writer);
			// Voter dist
			// Valence dist
			// Candidate points
			// Straw votes
			// Round 1 votes
			// Round 2 votes
		} catch (IOException e) {
			// FIXME react
		}
	}
	
	public static void write_player_info_for_game(Player player, int game_num, BufferedWriter writer) {
		try {
			writer.write("// Player: " + player.getPlayer_number());
			write_break(writer);
			// Ideal point
			// Actual valences
			// Initial expected payoffs
			// Buy 1 purchases
			// Expected payoffs
			// Straw vote
			// First vote
			// Buy 2 purchases
			// Expected payoffs
			// Last vote
			// Winnings
		} catch (IOException e) {
			// FIXME react
		}
	}

}
