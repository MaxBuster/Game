package client.UIHelpers;

import model.Candidate.CandidateInfo;
import utils.Constants.Constants;

import java.util.ArrayList;

public class TableGenerator {

	public static String[][] generate_info_table(ArrayList<CandidateInfo> candidate_info, int max_valence) {
		String[][] info_table_data = new String[candidate_info.size()][];
		int i = 0;
		for (CandidateInfo candidate : candidate_info) {
			String[] blank_row = Constants.CLIENT_INFO_ROW.clone();
			blank_row[0] = Integer.toString(candidate.candidate_num+1);
			blank_row[1] = Integer.toString(candidate.candidate_position);
			blank_row[2] = Integer.toString(candidate.expected_value) + " +/- " + max_valence;
			info_table_data[i] = blank_row;
			i++;
		}
		return info_table_data;
	}

	public static String[][] generate_buy_table(ArrayList<CandidateInfo> candidate_info) {
		String[][] buy_table_data = new String[candidate_info.size()][];
		int i = 0;
		for (CandidateInfo candidate : candidate_info) {
			String[] blank_row = Constants.CLIENT_BUY_ROW.clone();
			blank_row[0] = Integer.toString(candidate.candidate_num+1);
			blank_row[1] = Integer.toString(Constants.INFO_PRICE);
			buy_table_data[i] = blank_row;
			i++;
		}
		return buy_table_data;
	}

	public static String[][] generate_vote_table(ArrayList<CandidateInfo> candidate_info) {
		String[][] vote_table_data = new String[candidate_info.size()][];
		int i = 0;
		for (CandidateInfo candidate : candidate_info) {
			String[] blank_row = Constants.CLIENT_VOTE_ROW.clone();
			blank_row[0] = Integer.toString(candidate.candidate_num+1);
			vote_table_data[i] = blank_row;
			i++;
		}
		return vote_table_data;
	}

}
