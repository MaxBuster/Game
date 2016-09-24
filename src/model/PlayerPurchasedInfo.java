package model;

import java.util.HashMap;

/**
 * Keeps track of all info tokens received for one user throughout all games
 * @author Max Buster
 */

public class PlayerPurchasedInfo {
	private GameTokens[] allGameTokens; // Array of game tokens for each game
	
	public PlayerPurchasedInfo(int numGames, int[] numCandidates) {
		allGameTokens = new GameTokens[numGames];
		// Add game token object for each game
		for (int i=0; i<numGames; i++) {
			GameTokens gameTokens = new GameTokens(numCandidates[i]); // initialize with num cands
			allGameTokens[i] = gameTokens;
		}
	}
	
	/**
	 * Increments ones and total tokens for the specified game with round and candidate
	 */
	public synchronized void addOnesToken(int gameNum, String round, int candidateNum) {
		RoundTokens currentRoundInfo = getRoundTokens(gameNum, round);
		currentRoundInfo.all_tokens[candidateNum] += 1;
		currentRoundInfo.one_tokens[candidateNum] += 1;
	}
	
	/**
	 * Increments total tokens for the specified game with round and candidate
	 */
	public synchronized void addZerosToken(int gameNum, String round, int candidateNum) {
		RoundTokens currentRoundInfo = getRoundTokens(gameNum, round);
		currentRoundInfo.all_tokens[candidateNum] += 1;
	}
	
	public GameTokens[] getAllTokens() {
		return allGameTokens;
	}
	
	/**
	 * Get round tokens for a single round within a game
	 */
	public RoundTokens getRoundTokens(int gameNum, String round) {
		GameTokens currentGameInfo = allGameTokens[gameNum];
		RoundTokens currentRoundInfo = currentGameInfo.rounds_info.get(round);
		return currentRoundInfo;
	}
	
	// --------------------------------- Helper Classes --------------------------------------- //

	/**
	 * All tokens for both buy rounds for a single game
	 */
	public class GameTokens {
		protected HashMap<String, RoundTokens> rounds_info;
		
		public GameTokens(int numCandidates) {
			rounds_info = new HashMap<String, RoundTokens>();
			
			rounds_info.put(Constants.FIRST_BUY, new RoundTokens(numCandidates));
			rounds_info.put(Constants.SECOND_BUY, new RoundTokens(numCandidates));
		}
	}

	/**
	 * Ones tokens and all tokens received for all candidates for a single round
	 */
	public class RoundTokens {
		protected int[] one_tokens;
		protected int[] all_tokens;
		
		public RoundTokens(int numCandidates) {
			one_tokens = new int[numCandidates];
			all_tokens = new int[numCandidates];
		}
		
		public int[] getOnes() {
			return one_tokens;
		}
		
		public int[] getAll() {
			return all_tokens;
		}
	}
}
