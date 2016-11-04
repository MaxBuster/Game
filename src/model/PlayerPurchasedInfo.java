package model;

import java.util.ArrayList;

/**
 * Keeps track of all info tokens received for one user throughout all games
 * @author Max Buster
 */

public class PlayerPurchasedInfo {
	private ArrayList<Integer>[] purchased_candidates;
	//	private ArrayList<Token>[] tokens;

	public PlayerPurchasedInfo(int num_games) {
		purchased_candidates = new ArrayList[num_games];
		for (int i=0; i<num_games; i++) {
			purchased_candidates[i] = new ArrayList<Integer>();
		}
		//		tokens = new ArrayList[num_games];
		//		for (int i=0; i<num_games; i++) {
		//			tokens[i] = new ArrayList<Token>();
		//		}
	}

	// FIXME add round
	public void add_purchase(int game, int candidate) {
		if (!purchased_candidates[game].contains(candidate)) {
			purchased_candidates[game].add(candidate);
		}
	}

	public boolean purchased_info(int game, int candidate) {
		for (int cand : purchased_candidates[game]) {
			if (cand == candidate) {
				return true;
			}
		}
		return false;
	}

	//	public void add_token(int game, int value, String round, int candidate) {
	//		Token token = new Token(value, round, candidate);
	//		tokens[game].add(token);
	//	}
	//	
	//	public int[] get_tokens(int game, int candidate) {
	//		int[] candidate_info = new int[3];
	//		for (Token t : tokens[game]) {
	//			if (t.get_candidate() == candidate) {
	//				candidate_info[0] += t.get_value();
	//				candidate_info[1] += 1;
	//			}
	//		}
	//		candidate_info[2] = candidate;
	//		return candidate_info;
	//	}
	//	
	//	
	//	
	//	public class Token {
	//		protected int value;
	//		protected String round;
	//		protected int candidate;
	//		
	//		public Token(int value, String round, int candidate) {
	//			this.value = value;
	//			this.round = round;
	//			this.candidate = candidate;
	//		}
	//		
	//		public int get_value() {
	//			return value;
	//		}
	//		
	//		public String get_round() {
	//			return round;
	//		}
	//		
	//		public int get_candidate() {
	//			return candidate;
	//		}
	//	}
}
