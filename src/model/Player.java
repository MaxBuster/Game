package model;

public class Player {
	private final int player_number;
	private int ideal_point;
	private char player_party;
	private int winnings;
	private PlayerPurchasedInfo ppi;
	private int[] payoffs;
	private boolean done_with_round;
	
	public Player(int player_number, int num_games) {
		this.player_number = player_number;
		ppi = new PlayerPurchasedInfo(num_games);
		payoffs = new int[num_games];
		winnings = 0;
		done_with_round = false;
	}
	
	public void setPlayerInfo(int ideal_point, char player_party) {
		this.ideal_point = ideal_point;
		this.player_party = player_party;
	}

	public int getPlayer_number() {
		return player_number;
	}

	public int getIdeal_point() {
		return ideal_point;
	}

	public char getPlayer_party() {
		return player_party;
	}

	public PlayerPurchasedInfo getPpi() {
		return ppi;
	}

	public int[] getPayoffs() {
		return payoffs;
	}
	
	public void setPayoff(int gameNum, int payoff) {
		payoffs[gameNum] = payoff;
	}
	
	public int getWinnings() {
		return winnings;
	}
	
	public synchronized void setDone(boolean done) {
		this.done_with_round = done;
	}
	
	public synchronized boolean isDone() {
		return this.done_with_round;
	}
}
