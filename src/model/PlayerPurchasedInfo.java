package model;

import java.util.ArrayList;

/**
 * Keeps track of all info tokens received for one user throughout all games
 * @author Max Buster
 */

public class PlayerPurchasedInfo {
	private ArrayList<Integer>[] purchased_candidates;

	public PlayerPurchasedInfo(int num_games) {
		purchased_candidates = new ArrayList[num_games];
		for (int i=0; i<num_games; i++) {
			purchased_candidates[i] = new ArrayList<Integer>();
		}
	}

	// FIXME add round, add get all info
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
}
