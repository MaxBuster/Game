/**
 * Contains static info for a player and a list of game info that updates each game
 */

package model.Player;

import model.Game.Game;

import java.util.ArrayList;

public class Player {
	private final int player_number;
	private int winnings;
	private ArrayList<PlayerGameInfo> pgi_list;
	private boolean done_with_round;
	
	public Player(int player_number) {
		this.player_number = player_number;
		this.winnings = 0;
		this.pgi_list = new ArrayList<PlayerGameInfo>();
		this.done_with_round = false;
	}
	
	public PlayerGameInfo new_pgi(Game game) {
		PlayerGameInfo pgi = new PlayerGameInfo(game);
		pgi_list.add(pgi);
		return pgi;
	}
	
	public PlayerGameInfo get_pgi(int game_num) {
		return pgi_list.get(game_num);
	}
	
	public int num_games_played() {
		return pgi_list.size();
	}

	public int get_player_number() {
		return player_number;
	}
	
	public void add_winnings(int payoff) {
		winnings += payoff;
	}
	
	public int get_winnings() {
		return winnings;
	}
	
	public synchronized void set_done(boolean done) {
		this.done_with_round = done;
	}
	
	public synchronized boolean is_done() {
		return this.done_with_round;
	}
}
