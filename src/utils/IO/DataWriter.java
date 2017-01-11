package utils.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Game.Game;
import model.Model;
import model.Player.Player;
import model.Player.PlayerGameInfo;
import utils.Constants.Constants;

public class DataWriter {

	public static void write_data(String file_name, Model model) {
		File file;
		BufferedWriter writer = null;
		try {
			file = new File(file_name);
			writer = new BufferedWriter(new FileWriter(file));
			ArrayList<Player> players = model.get_players();
			for (int i=0; i<players.size(); i++) {
				write_player_info_for_game(players.get(i), writer, model);
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
		writer.write("// Game: " + game.get_game_num());
		write_break(writer);
	}

	public static void write_player_info_for_game(Player player, BufferedWriter writer, Model model) throws IOException {
		writer.write("// Player: " + player.get_player_number());
		write_break(writer);
		for (int i=0; i<model.get_current_game_num(); i++) {
			PlayerGameInfo pgi = player.get_pgi(i);
			writer.write("// Game: " + i);
			write_break(writer);
			writer.write("// Player Position: " + pgi.get_voter_position());
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
		String s = "";
		for (int i=0; i<data.length-1; i++) {
			s += string_data[i] + ',';
		}
		s += string_data[data.length-1];
		return s;
	}

	public static int[] bool_to_int(boolean[] bool_data) {
		int[] int_data = new int[bool_data.length];
		for (int i=0; i<bool_data.length; i++) {
			int_data[i] = bool_data[i] ? 1 : 0;
		}
		return int_data;
	}
}