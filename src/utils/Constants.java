package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Holds labels that can be referenced across all classes
 * @author Max Buster
 */

public class Constants {
	// Server PCS
	public static final String NEW_GAME = "New Game";
	public static final String NEW_ROUND = "New Round";
	public static final String ROUND_OVER = "Round Over";
	public static final String REMOVE_PLAYER = "Remove Player";

	// Buy round names
	public static final String FIRST_BUY = "Buy 1";
	public static final String SECOND_BUY = "Buy 2";

	// Vote round names
	public static final String STRAW_VOTE = "Straw Vote";
	public static final String FIRST_VOTE = "First Vote";
	public static final String FINAL_VOTE = "Final Vote";

	// Edge rounds
	public static final String ALL_FINISHED = "All Finished";
	public static final int NOT_STARTED = -1;

	// Round List
	public static final String[] LIST_OF_ROUNDS = new String[]{
			FIRST_BUY, STRAW_VOTE, FIRST_VOTE, SECOND_BUY, FINAL_VOTE
	}; 

	// Return codes
	public static final int IOEXCEPTION = 1;
	public static final int METHOD_SUCCEEDED = 0;

	// Server Events
	public static final String START_GAME = "Start Game";
	public static final String WRITE_DATA = "Write Data";
	public static final String NEW_PLAYER = "New Player";
	public static final String END_ALL_GAMES = "End All Games";
	public static final String PLAYER_WINNINGS = "Winnings";

	// Message Types
	public static final int MESSAGE_START = -1;
	public static final int START_INFO = 1;
	public static final int PLAYER_INFO = 2;
	public static final int GAME_INFO = 3;
	public static final int VOTER_DIST = 4;
	public static final int ALL_CANDIDATES = 5;
	public static final int ROUND_NUMBER = 6;
	public static final int WINNINGS = 7;
	public static final int CANDIDATE_PAYOFF = 8;
	public static final int STRAW_VOTES = 9;
	public static final int FIRST_VOTES = 10;
	public static final int CANDIDATE_NUMS = 11;
	public static final int END_OF_GAME = 12;
	
	public static final int REQUEST_INFO = 20;
	public static final int END_ROUND = 21;
	public static final int VOTE = 22;
	
	// Empty Message
	public static final int[] EMPTY_MESSAGE = new int[0];

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
	
	// Graph Colors
	public static final Color[] GRAPH_GOLORS = new Color[]{
			Color.GREEN, Color.BLACK, Color.BLUE, Color.ORANGE,
			Color.PINK, Color.CYAN, Color.DARK_GRAY, Color.RED, 
			Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA,
			Color.WHITE, Color.YELLOW};

	// Panel Visibility
	// Order: [game_labels, player_labels, all_info, action_table, end_round]
	public static final boolean[] START_GAME_VISIBILITY = new boolean[]{false, false, false, false, false};
	public static final boolean[] BUY_ROUND_VISIBILITY = new boolean[]{true, true, true, true, true};
	public static final boolean[] VOTE_ROUND_VISIBILITY = new boolean[]{true, true, true, true, false};
	public static final boolean[] END_ROUND_VISIBILITY = new boolean[]{true, true, true, false, false};
	public static final boolean[] END_GAME_VISIBILITY = new boolean[]{true, false, false, false, false};

	// Client table headers
	public static final String[] INFO_TABLE_HEADERS = new String[] {
			"Candidate #", "Position", "Estimated Payoff", "Straw Votes", "First Round Votes"
	};
	public static final String[] BUY_TABLE_HEADERS = new String[] {
			"Candidate #", "Price", "Action"
	};
	public static final String[] VOTE_TABLE_HEADERS = new String[] {
			"Candidate #", "Action"	
	};
	
	// Server table headers
	public static final String[] PLAYER_HEADERS = new String[]{
			"Player #", "Winnings", "Remove"
	};

	// Client table blank rows
	// Order: Candidate #, Position, Payoff Estimate, Straw Votes, First Round Votes
	public static final String[] CLIENT_INFO_ROW = new String[]{"--", "--", "--", "--", "--"};
	// Order: Candidate #, Price, Buy Button
	public static final String[] CLIENT_BUY_ROW = new String[]{"--", "--", "Buy"};
	// Order: Candidate #, Vote Button
	public static final String[] CLIENT_VOTE_ROW = new String[]{"--", "Vote"};

	// Zero token array for candidate beta distribution
	public static final int[] ZERO_TOKENS = new int[]{0, 0};
	
	// Info Prices
	public static final int INFO_PRICE = 1;
}
