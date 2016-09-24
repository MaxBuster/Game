package model;

/**
 * Holds labels that can be referenced across all classes
 * @author Max Buster
 */

public class Constants {
	// Buy round names
	public static final String FIRST_BUY = "Buy1";
	public static final String SECOND_BUY = "Buy2";
	
	// Vote round names
	public static final String STRAW_VOTE = "Straw";
	public static final String FIRST_VOTE = "First";
	public static final String FINAL_VOTE = "Final";

	// Edge rounds
	public static final String NOT_STARTED = "Not Started";
	public static final String ALL_FINISHED = "All Finished";
	
	// Return codes
	public static final int IOEXCEPTION = 1;
	public static final int METHOD_SUCCEEDED = 0;
	
	// Parties
	public static final char PARTY_1 = 'D';
	public static final char Party_2 = 'R';
	
	// Server Events
	public static final String START_GAME = "Start Game";
	public static final String END_GAME = "End Game";
	public static final String WRITE_DATA = "Write Data";
}
