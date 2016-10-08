package model;

public class Player {
	private final int player_number;
	private int ideal_point;
	private char player_party;
	private int winnings;
	private PlayerPurchasedInfo ppi;
	private int[] payoffs;
	
	public Player(int player_number, int num_games, int[] candidates_per_game) {
		this.player_number = player_number;
		ppi = new PlayerPurchasedInfo(num_games, candidates_per_game);
		payoffs = new int[num_games];
		winnings = 0;
	}
	
	public void setPlayerInfo(int ideal_point, char player_party) {
		this.ideal_point = ideal_point;
		this.player_party = player_party;
	}
	
	public void addOnesToken(int currentGame, String currentRound, int candidateNum) {
		ppi.addOnesToken(currentGame, currentRound, candidateNum);
	}
	
	public void addZerosToken(int currentGame, String currentRound, int candidateNum) {
		ppi.addZerosToken(currentGame, currentRound, candidateNum);
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
}
