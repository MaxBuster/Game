package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ClientIOHandler {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private DataInputStream in;
	private DataOutputStream out;
	private ClientGUI gui;
	
	public ClientIOHandler(DataInputStream in, DataOutputStream out) {
		pcs.addPropertyChangeListener(new ChangeListener());
		this.in = in;
		this.out = out;
		gui = new ClientGUI(pcs);
		gui.setVisible(true);
	}
	
	public void handleIO() {
		while (true) {
			/**
			 * TODO
			 * Beginning info
			 * Game start
			 * New game info
			 * Purchased info
			 * Start round: Buy1, Straw, Vote1, Buy2, Final
			 * Vote count received: Straw, Vote1, Final
			 * Candidates that moved on: Vote1 results
			 * Candidate won + winnings 
			 * Games over
			 * Removed from game
			 */
		}
	}
	
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			/**
			 * Purchase info about candidate
			 * Done with buy round
			 * Vote
			 */
		}
	}

}
