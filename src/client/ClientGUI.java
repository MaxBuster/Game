package client;

import java.beans.PropertyChangeSupport;

import javax.swing.JFrame;

public class ClientGUI extends JFrame {
	private PropertyChangeSupport pcs;
	
	/**
	 * TODO
	 * Labels: Player #, Party, Ideal Pt, Budget, Winnings
	 * Labels: Game #/Total, Round
	 * Text box: Info box
	 * Table 1: Candidate info table
	 * Table 2: Buy/Vote action table
	 * Chart: Voter, candidate, ideal pt chart
	 */
	
	public ClientGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
		
		/**
		 * Style:
		 * Player label panel
		 * Game label panel
		 * Info + Actions tables panel
		 * Graph panel
		 * End round btn panel
		 */
	}
}
