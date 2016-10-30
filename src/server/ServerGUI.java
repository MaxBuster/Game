package server;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import utils.ButtonEditor;
import utils.ButtonRenderer;
import utils.Constants;

/**
 * Creates a UI for the RA to interact with and track game progress
 * @author Max Buster
 */

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L; // Default serial version ID
	private PropertyChangeSupport pcs;
	private JPanel content;
	
	private JTable player_table;
	private Object[][] player_table_data;
	
	// Changeable labels
	private Label current_game_change;
	private Label current_round_change;
	
	/**
	 * Initialize the UI with all the needed components
	 * @param pcs
	 */
	public ServerGUI(PropertyChangeSupport pcs, int num_games) {
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());
		this.player_table_data = new Object[0][];
		this.content = new JPanel(new GridLayout(3, 1, 0, 5));
		this.content.setBorder(new EmptyBorder(100, 100, 100, 100));
		add_game_label_panel(num_games);
		add_game_controls_panel();
		add_players_table();
		setContentPane(this.content);
		set_players_table();
				
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
//		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); FIXME do this when it's ready
	}
	
	/**
	 * Add labels which show game progress
	 */
	private void add_game_label_panel(int num_games) {
		Label current_game_fixed = new Label("Current Game: ", Label.RIGHT);
		current_game_fixed.setFont(Constants.BIG_BOLD_LABEL);
		current_game_change = new Label("0", Label.LEFT);
		current_game_change.setFont(Constants.BIG_LABEL);
		JPanel current_game_panel = new JPanel();
		current_game_panel.add(current_game_fixed);
		current_game_panel.add(current_game_change);

		Label num_games_fixed = new Label("Number of Games: ", Label.RIGHT);
		num_games_fixed.setFont(Constants.BIG_BOLD_LABEL);
		Label num_games_change = new Label(Integer.toString(num_games), Label.LEFT);
		num_games_change.setFont(Constants.BIG_LABEL);
		JPanel num_games_panel = new JPanel();
		num_games_panel.add(num_games_fixed);
		num_games_panel.add(num_games_change);
		
		Label current_round_fixed = new Label("Current Round: ", Label.RIGHT);
		current_round_fixed.setFont(Constants.BIG_BOLD_LABEL);
		current_round_change = new Label("Not Started", Label.LEFT);
		current_round_change.setFont(Constants.BIG_LABEL);
		JPanel current_round_panel = new JPanel();
		current_round_panel.add(current_round_fixed);
		current_round_panel.add(current_round_change);
		
		JPanel game_label_panel = new JPanel();
		game_label_panel.add(current_game_panel);
		game_label_panel.add(num_games_panel);
		game_label_panel.add(current_round_panel);
		
		content.add(game_label_panel);
	}
	
	/**
	 * Add buttons that allow you to start and end the game as well as 
	 * write the game data out to a data file
	 */
	private void add_game_controls_panel() {
		final Button start_game = new Button(Constants.START_GAME);
		start_game.setPreferredSize(Constants.BIG_BUTTON);
		start_game.setFont(Constants.BIG_LABEL);
		start_game.setBackground(Constants.GREEN);
		final Button end_game = new Button(Constants.END_GAME);
		end_game.setPreferredSize(Constants.BIG_BUTTON);
		end_game.setFont(Constants.BIG_LABEL);
		end_game.setBackground(Constants.RED);
		final Button write_data = new Button(Constants.WRITE_DATA);
		write_data.setPreferredSize(Constants.BIG_BUTTON);
		write_data.setFont(Constants.BIG_LABEL);
		write_data.setBackground(Constants.BLUE);
		
		final JPanel game_controls_panel = new JPanel();
		game_controls_panel.add(start_game);
		game_controls_panel.add(end_game);
		game_controls_panel.add(write_data);
		
		content.add(game_controls_panel);
		
		start_game.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pcs.firePropertyChange(Constants.START_GAME, null, null);
				remove_component_and_update(game_controls_panel, start_game);
			}
		});
		end_game.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// FIXME add dialogue to confirm they're sure
				pcs.firePropertyChange(Constants.END_GAME, null, null);
				remove_component_and_update(game_controls_panel, end_game);
			}
		});
		write_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pcs.firePropertyChange(Constants.WRITE_DATA, null, null);
				// TODO add some sort of dialog or response
			}
		});
	}
	
	private void remove_component_and_update(JPanel panel, Component component) {
		panel.remove(component);
		panel.revalidate();
		panel.repaint();
	}
	
	
	
	private void set_players_table() {
		final String[] headers = Constants.PLAYER_HEADERS;
		Object[][] data = player_table_data;
		DefaultTableModel player_table_model = new DefaultTableModel(data, headers) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				if ((headers.length-1) == column) {
					return true;
				}
				return false;
			}
		};
		player_table.setModel(player_table_model);
		
		int button_column = headers.length - 1;
		String column_name = headers[button_column];
		player_table.getColumn(column_name).setCellRenderer(new ButtonRenderer());
		player_table.getColumn(column_name).setCellEditor(new ButtonEditor(pcs, player_table)); 
	}
	
	/**
	 * Add the table which shows player connections with buttons that
	 * allow the player to be removed from the game
	 */
	private void add_players_table() {
		player_table = new JTable(null);
		Dimension d = new Dimension(5, 5);
		player_table.setPreferredScrollableViewportSize(d);
		JScrollPane scroll_table = new JScrollPane(player_table);
		content.add(scroll_table);
	}
	
	/**
	 * Add a row to the player table to remove that player
	 */
	private void add_player_to_table(int player_num) {
		int player_visible_num = player_num + 1;
		String player_string = Integer.toString(player_visible_num);
		String[] player_row = new String[]{player_string, "Remove"};
		int current_length = player_table_data.length;
		add_row_to_table();
		player_table_data[current_length] = player_row;
		set_players_table();
	}
	
	/**
	 * Extend table by a row
	 */
	private void add_row_to_table() {
		Object[][] extended_data = new Object[player_table_data.length+1][];
		for (int i=0; i<player_table_data.length; i++) {
			extended_data[i] = player_table_data[i];
		}
		player_table_data = extended_data;
	}
	
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			if (event == Constants.NEW_GAME) {
				int current_game = (Integer) PCE.getOldValue();
				int current_game_viewable = current_game + 1;
				current_game_change.setText(Integer.toString(current_game_viewable));
			} else if (event == Constants.NEW_ROUND) {
				current_round_change.setText(Constants.LIST_OF_ROUNDS[(Integer) PCE.getOldValue()]);
			} else if (event == Constants.NEW_PLAYER) {
				int player_num = (Integer) PCE.getOldValue();
				add_player_to_table(player_num);
				// TODO add player to gui with button to remove
			}
		}
	}
}
