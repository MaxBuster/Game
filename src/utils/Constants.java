package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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
	public static final int PARTY_1 = 1;
	public static final int Party_2 = 2;
	public static final String PARTY_1_NAME = "D";
	public static final String Party_2_NAME = "R";
	
	// Server Events
	public static final String START_GAME = "Start Game";
	public static final String END_GAME = "End Game";
	public static final String WRITE_DATA = "Write Data";
	
	// Message Types
	public static final int MESSAGE_START = -1;
	public static final int START_INFO = 1;
	public static final int PLAYER_INFO = 2;
	public static final int GAME_INFO = 3;
	public static final int VOTER_DIST = 4;
	
	// Fonts
	public static final Font BIG_BOLD_LABEL = new Font("Serif", Font.BOLD, 30);
	public static final Font BIG_LABEL = new Font("Serif", Font.PLAIN, 30);
	public static final Font MEDIUM_BOLD_LABEL = new Font("Serif", Font.BOLD, 20);
	public static final Font MEDIUM_LABEL = new Font("Serif", Font.PLAIN, 20);

	// Dimensions
	public static final Dimension BIG_BUTTON = new Dimension(300, 50);

	// Colors
	public static final Color GREEN = new Color(153, 255, 153);
	public static final Color RED = new Color(255, 153, 153);
	public static final Color BLUE = new Color(153, 204, 255);
	
	// Panel Visibility
	// Order: [game_labels, player_labels, all_info, action_table, end_round]
	public static final boolean[] START_GAME_VISIBILITY = new boolean[]{false, false, false, false, false};
	public static final boolean[] START_NEW_GAME_VISIBILITY = new boolean[]{true, true, true, true, true};
	public static final boolean[] END_ROUND_VISIBILITY = new boolean[]{true, true, true, false, false};
	public static final boolean[] END_GAME_VISIBILITY = new boolean[]{true, false, false, false, false};
}
