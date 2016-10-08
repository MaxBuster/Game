package server;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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
	private String[] player_table_column_names;
	private Object[][] player_table_data;
	
	// Changeable labels
	private Label current_game_change;
	private Label current_round_change;
	
	/**
	 * TODO:
	 * Update table on player removed/added
	 * 
	 * Style:
	 * Change text, buttons background style
	 * Change spacing so it doesn't fit width
	 */
	
	/**
	 * Initialize the UI with all the needed components
	 * @param pcs
	 */
	public ServerGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());
		this.player_table_column_names = new String[]{"Player #", "Remove"}; // FIXME make constants
		this.player_table_data = new Object[0][2];
		this.content = new JPanel(new GridLayout(3, 1, 0, 5));
		this.content.setBorder(new EmptyBorder(100, 100, 100, 100));
		add_game_label_panel();
		add_game_controls_panel();
		add_players_table();
		setContentPane(this.content);
				
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
//		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); FIXME do this when it's ready
	}
	
	/**
	 * Add labels which show game progress
	 */
	private void add_game_label_panel() {
		Label current_game_fixed = new Label("Current Game: ", Label.RIGHT);
		current_game_fixed.setFont(Constants.BIG_BOLD_LABEL);
		current_game_change = new Label("0", Label.LEFT);
		current_game_change.setFont(Constants.BIG_LABEL);
		JPanel current_game_panel = new JPanel();
		current_game_panel.add(current_game_fixed);
		current_game_panel.add(current_game_change);

		Label num_games_fixed = new Label("Number of Games: ", Label.RIGHT);
		num_games_fixed.setFont(Constants.BIG_BOLD_LABEL);
		Label num_games_change = new Label("0", Label.LEFT);
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
	
	private void set_players_table(final String[] headers, String[][] data) {
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
		player_table.getColumn(column_name).setCellEditor(new ButtonEditor(pcs)); 
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
	
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			if (event == Constants.NEW_GAME) {
				current_game_change.setText((String) PCE.getOldValue());
			} else if (event == Constants.NEW_ROUND) {
				current_round_change.setText(Constants.LIST_OF_ROUNDS[(Integer) PCE.getOldValue()]);
			}
		}
	}
}
