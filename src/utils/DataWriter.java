package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Game;
import model.Model;
import model.Player;
import model.PlayerGameInfo;

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
						write_break(writer);
					}	
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
			Integer[] straw_votes = game.get_round_votes(Constants.STRAW_VOTE);
			// Round 1 votes
			Integer[] first_votes = game.get_round_votes(Constants.FIRST_VOTE);
			// Round 2 votes
			Integer[] final_votes = game.get_round_votes(Constants.FINAL_VOTE);
		} catch (IOException e) {
			// FIXME react
		}
	}
	
	public static void write_player_info_for_game(Player player, int game_num, BufferedWriter writer) {
		try {
			writer.write("// Player: " + player.getPlayer_number());
			write_break(writer);
			
			PlayerGameInfo pgi = player.get_pgi(game_num);
			// Ideal point
			pgi.get_ideal_pt();
			// Actual valences
			pgi.get_valences();
			// Initial expected payoffs
			// Buy 1 purchases
			pgi.get_purchases(Constants.FIRST_BUY);
			// Expected payoffs
			// Straw vote
			// First vote
			// Buy 2 purchases
			pgi.get_purchases(Constants.SECOND_BUY);
			// Expected payoffs
			// Last vote
			pgi.get_votes();
			// Winnings
			pgi.get_winnings();
		} catch (IOException e) {
			// FIXME react
		}
	}

}
