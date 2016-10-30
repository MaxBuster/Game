package model;

public class Player {
	private final int player_number;
	private int ideal_point;
	private int winnings;
	private PlayerPurchasedInfo ppi;
	private int[] payoffs; // FIXME what for?
	private boolean done_with_round;
	private int[] valences;
	private int budget;
	
	public Player(int player_number, int num_games) {
		this.player_number = player_number;
		ppi = new PlayerPurchasedInfo(num_games);
		payoffs = new int[num_games];
		winnings = 0;
		done_with_round = false;
		budget = 0;
	}
	
	public void setPlayerInfo(int ideal_point, int[] valences, int budget) {
		this.ideal_point = ideal_point;
		this.valences = valences;
		this.budget = budget;
	}
	
	public int get_valence_for_cand(int candidate_num) {
		return valences[candidate_num];
	}

	public int getPlayer_number() {
		return player_number;
	}

	public int getIdeal_point() {
		return ideal_point;
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
	
	public void subtract_budget(int price) {
		budget -= price;
	}
	
	public int get_budget() {
		return budget;
	}
}
