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
		
	}
	
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			// TODO react to gui events
		}
	}

}
