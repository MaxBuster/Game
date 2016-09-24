package server;

import java.beans.PropertyChangeSupport;

import javax.swing.JFrame;

public class ServerGUI extends JFrame {
	private PropertyChangeSupport pcs;
	
	/**
	 * TODO
	 * Labels: Current game/Total, current round
	 * Start Games btn
	 * End Games btn
	 * Write Data btn
	 * Table of players and buttons to remove them
	 */
	
	public ServerGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
	}
}
