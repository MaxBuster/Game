package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import model.Model;
import model.Player;

public class ServerIOHandler {
	private Model model;
	private Player player;
	private DataInputStream in;
	private DataOutputStream out;
	private static Object waitObject = new Object();
//	private int gameNum = 0; FIXME do we need this?

	public ServerIOHandler(Model model, DataInputStream in, DataOutputStream out) {
		this.model = model;
		this.player = model.new_player();
		this.in = in;
		this.out = out;
	}
	
	public int getPlayerNum() {
		return player.getPlayer_number();
	}
	
	public void handleIO() {
		// TODO send player number and initial instructions?
		while (!model.game_started()) {
			sleep(200);
		}
		// TODO send info
		while (true) {
			/**
			 * TODO add io statements:
			 * Purchase info request
			 * Player done with buy1, buy2
			 * Player voted straw, first, second
			 * Error?
			 */
		}
	}

	private void sleep(long sleep_time_ms) {
		try {
			Thread.sleep(sleep_time_ms);
		} catch (InterruptedException e) {
			e.printStackTrace(); // FIXME do more
		}
	}
}
