package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VoteHandler {
	
	public static int[] get_top_x(int[] candidates_with_votes, int x) {
		ArrayList<Pair> candidate_list = get_pairs(candidates_with_votes);
		Collections.sort(candidate_list, new VotesComparator());
		for (int i=candidate_list.size()-1; i>x+1; i--) {
			candidate_list.remove(i);
		}
		Collections.sort(candidate_list, new CandidateNumComparator());
		int[] top_x = new int[x];
		for (int i=0; i<x; i++) {
			top_x[i] = candidate_list.get(i).candidate_num;
		}
		return top_x;
	}
	
	public static ArrayList<Pair> get_pairs(int[] candidates_with_votes) {
		ArrayList<Pair> candidate_list = new ArrayList<Pair>();
		for (int i=0, j=1; j<candidates_with_votes.length; i+=2, j+=2) {
			Pair p = new Pair(candidates_with_votes[i], candidates_with_votes[j]);
			candidate_list.add(p);
		}
		return candidate_list;
	}
	
	// ------------------------------ Private Classes ----------------------------------- //
			
	private static class Pair {
		public int candidate_num;
		public int votes;
		
		public Pair(int candidate_num, int votes) {
			this.candidate_num = candidate_num;
			this.votes = votes;
		}
	}
	
	private static class CandidateNumComparator implements Comparator<Pair> {
	    @Override
	    public int compare(Pair a, Pair b) {
	        return a.candidate_num < b.candidate_num ? -1 : a.candidate_num == b.candidate_num ? 0 : 1;
	    }
	}
	
	private static class VotesComparator implements Comparator<Pair> {
	    @Override
	    public int compare(Pair a, Pair b) {
	        return a.votes < b.votes ? -1 : a.votes == b.votes ? 0 : 1;
	    }
	}
}
