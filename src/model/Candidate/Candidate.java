/**
 * Contains candidate data
 * @author Max Buster
 */

package model.Candidate;

public class Candidate {
	private final int candidate_number;
	private final int position;

	public Candidate(int candidate_number, int position) {
		this.candidate_number = candidate_number;
		this.position = position;
	}
	
	public int get_candidate_num() { return this.candidate_number; }
	
	public int get_position() { return this.position; }
}
