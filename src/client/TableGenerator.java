package client;

import utils.Constants;

public class TableGenerator {

	public static String[][] generate_info_table(int[] candidate_info, int max_valence) {
		String[][] info_table_data = new String[candidate_info.length/3][];
		for (int i=0, j=0; i<candidate_info.length; i+=3, j++) {
			int candidate_number = candidate_info[i];
			int candidate_viewable_number = candidate_number + 1; // Note - this increments the cand number to make it work
			int candidate_ideal_pt = candidate_info[i+1];
			int expected_payoff = candidate_info[i+2];
			
			String[] blank_row = Constants.CLIENT_INFO_ROW.clone();
			blank_row[0] = Integer.toString(candidate_viewable_number);
			blank_row[1] = Integer.toString(candidate_ideal_pt);
			blank_row[2] = Integer.toString(expected_payoff) + " +/- " + max_valence;
			info_table_data[j] = blank_row;
		}
		return info_table_data;
	}

	public static String[][] generate_buy_table(int[] candidate_nums) {
		String[][] buy_table_data = new String[candidate_nums.length][];
		for (int i=0; i<candidate_nums.length; i++) {
			int candidate_number = candidate_nums[i];
			int candidate_viewable_number = candidate_number + 1; // Note - this increments the cand number to make it work
			
			String[] blank_row = Constants.CLIENT_BUY_ROW.clone();
			blank_row[0] = Integer.toString(candidate_viewable_number);
			blank_row[1] = Integer.toString(Constants.INFO_PRICE);

			buy_table_data[i] = blank_row;
		}
		return buy_table_data;
	}

	public static String[][] generate_vote_table(int[] candidate_nums) {
		String[][] vote_table_data = new String[candidate_nums.length][];
		for (int i=0; i<candidate_nums.length; i++) {
			int candidate_number = candidate_nums[i];
			int candidate_viewable_number = candidate_number + 1; // Note - this increments the cand number to make it work
			
			String[] blank_row = Constants.CLIENT_VOTE_ROW.clone();
			blank_row[0] = Integer.toString(candidate_viewable_number);
			vote_table_data[i] = blank_row;
		}
		return vote_table_data;
	}

}
