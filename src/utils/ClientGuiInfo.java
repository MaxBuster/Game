package utils;

public class ClientGuiInfo {
	
	public static final String NOT_STARTED = 
			"Please read the instructions packet and wait for the games to begin";

	public static final String BUY_1 =
			"You currently have expected payoffs that you would receive for each candidate if they were to win the final vote. \n"
			+ "However, these expectations do not include a random hidden value that is added onto the payoff if that candidate wins. \n"
			+ "These additional values are generated randomly for each candidate and are specific to you so they aren't influenced by anyone else's values. \n"
			+ "If you click buy next to the candidate number in the action table, the expected value will update in the table with the actual value you would receive if that candidate won. \n"
			+ "However, you must also note that if you do not purchase any information, your budget will be added to the ultimate payoff. "
			+ "The random values for this round will be: ";
	
	public static final String WAIT = 
			"Please wait while everyone finishes this round.";
	
	public static final String STRAW = 
			"It is now the straw vote. \n\n"
			+ "This vote will have no effect on the candidates but is merely to show you who the other voters are voting for";
	
	public static final String FIRST = 
			"This is the first real vote. \n\n"
			+ "The top two candidates from this round will continue to the final voting round.";
	
	public static final String BUY_2 = 
			"This is the final information purchase round.";
	
	public static final String FINAL = 
			"This is the final vote round. \n\n"
			+ "Whoever wins this round will win the election.";
	
	public static final String FINISHED = 
			"All games are finished \n\n"
			+ "Please wait for further instructions";
}
