package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import model.Game;
import model.Model;
import model.Player;
import model.PlayerGameInfo;

public class DataWriter {

	public static void write_data(String file_name, Model model) {
		File file;
		BufferedWriter writer = null;
		try {
			file = new File(file_name);
			writer = new BufferedWriter(new FileWriter(file));
			ArrayList<Player> players = model.get_players();
			for (int i=0; i<players.size(); i++) {
				write_player_info_for_game(players.get(i), writer);
				write_break(writer);
			}
			writer.flush();
		} catch (IOException e) {
			// FIXME return false?
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void write_break(BufferedWriter writer) throws IOException {
		writer.write("\n");
	}

	public static void write_game_info(Game game, BufferedWriter writer) throws IOException {
		writer.write("// Game: " + game.getGameNumber());
		write_break(writer);
	}

	public static void write_player_info_for_game(Player player, BufferedWriter writer) throws IOException {
		writer.write("// Player: " + player.getPlayer_number());
		for (int i=0; i<player.num_games_played(); i++) {
			PlayerGameInfo pgi = player.get_pgi(i);
			writer.write("// Game: " + i);
			write_break(writer);
			writer.write("// Ideal point: " + pgi.get_ideal_pt());
			write_break(writer);
			writer.write("// Valences: " + implode(pgi.get_valences()));
			write_break(writer);
			writer.write("// Round 1 Buys: " + implode(bool_to_int(pgi.get_purchases(Constants.FIRST_BUY))));
			write_break(writer);
			writer.write("// Round 2 Buys: " + implode(bool_to_int(pgi.get_purchases(Constants.SECOND_BUY))));
			write_break(writer);
			writer.write("// Votes: " + implode(pgi.get_votes()));
			write_break(writer);
			writer.write("// Winnings: " + pgi.get_winnings());
			write_break(writer);
		}
	}

	public static String implode(int[] data) {
		String[] string_data = new String[data.length];
		for (int i=0; i<data.length; i++) {
			string_data[i] = Integer.toString(data[i]);
		}
		return String.join(",", Arrays.asList(string_data));
	}

	public static int[] bool_to_int(boolean[] bool_data) {
		int[] int_data = new int[bool_data.length];
		for (int i=0; i<bool_data.length; i++) {
			int_data[i] = bool_data[i] ? 1 : 0;
		}
		return int_data;
	}
}
