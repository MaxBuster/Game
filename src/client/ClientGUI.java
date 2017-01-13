/**
 * Creates the UI for the client and contains methods to update the UI based on server side events
 */

package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import client.UIHelpers.ChartCreator;
import client.UIHelpers.ClientGuiInfo;
import client.UIHelpers.TableGenerator;
import utils.Distributions.VoterDistribution;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.TextAnchor;

import utils.UI.ButtonEditor;
import utils.UI.ButtonRenderer;
import utils.Constants.Constants;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = 1L; // Default serial id
	private PropertyChangeSupport pcs; // Provides communication btwn UI and Client Handler
	private JPanel content; // Panel that holds all of the UI

	/* All game labels */
	private Label current_game_change;
	private Label num_games_change;
	private Label current_round_change;
	private Label winnings_change;

	/* All player labels */
	private Label player_number_change;
	private Label ideal_point_change;
	private Label budget_change;

	/* Info text */
	private JTextArea info_block;
	private String buy_info;

	/* Label containers */
	private JPanel current_round;
	private JPanel game_label_panel;
	private JPanel player_label_panel;

	/* Top level container of all current game info */
	private JPanel all_info;

	/* Containers for the action and info tables */
	private JPanel tables;
	private JTable info_table;
	private JScrollPane info_table_pane;
	private JTable action_table;
	private JScrollPane action_table_pane;

	/* Container to hold the end round button for info rounds */
	private JPanel end_round_panel;

	/* JFreeChart objects */
	private ChartPanel chart;
	private ValueMarker marker;
	private ArrayList<ValueMarker> all_markers;

	/* Table data */
	String[][] info_table_data;
	String[][] buy_table_data;
	String[][] vote_table_data;

	/**
	 * Initialize the Swing GUI with all the components
	 */
	public ClientGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
		this.all_markers = new ArrayList<ValueMarker>();

		/* Initialize the main UI panel */
		this.content = new JPanel();
		this.content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		this.content.setBorder(new EmptyBorder(100, 100, 100, 100));

		/* Add all sub panels to the main content panel */
		add_game_label_panel();
		add_player_label_panel();
		add_info_panel();

		/* Add all tables to the top level container */
		this.tables = new JPanel();
		this.tables.setLayout(new BoxLayout(tables, BoxLayout.Y_AXIS));
		this.tables.add(info_table_pane);
		this.tables.add(action_table_pane);

		/* Set panels that will be changed depending on the round */
		this.info_table_pane = add_info_table();
		this.action_table_pane = add_action_table();
		this.chart = add_chart();

		/* Add all the info panels to the top level container */
		this.all_info = new JPanel();
		this.all_info.setLayout(new BoxLayout(all_info, BoxLayout.X_AXIS));
		this.all_info.add(tables);
		this.all_info.add(chart);
		this.content.add(all_info);

		/* Add the end round panel to the UI */
		add_end_round_panel();

		/* Set config of initial starting screen */
		setContentPane(this.content);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		set_visible_panels(Constants.START_GAME_VISIBILITY);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	// -------------------------------------- General Pane ----------------------------------------- //

	/**
	 * Takes a constant boolean array and sets the visibility of the panels
	 * according to the values in the array
	 * @param visible - the array of booleans which signifies which panels should be visible
	 */
	public void set_visible_panels(final boolean[] visible) {
		game_label_panel.setVisible(visible[0]);
		player_label_panel.setVisible(visible[1]);
		all_info.setVisible(visible[2]);
		action_table_pane.setVisible(visible[3]);
		end_round_panel.setVisible(visible[4]);
		content.revalidate();
		content.repaint();
	}

	/**
	 * Allows the Client UI to close on exit button
	 */
	public void set_default_close() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// -------------------------------------- Setters ----------------------------------------- //

	/**
	 * Sets labels that won't change throughout all the games
	 * @param player_num - the number of this client's player
	 * @param num_games - the number of games we will be playing today
	 */
	public void set_start_info(int player_num, int num_games) {
		player_number_change.setText(Integer.toString(player_num + 1)); // Make num readable
		num_games_change.setText(Integer.toString(num_games));
	}

	/**
	 * Set ideal point label and chart marker for the current game
	 * @param player_position - this client's position on the number line
	 */
	public void set_player_info(int player_position) {
		ideal_point_change.setText(Integer.toString(player_position));
		remove_all_markers();
		add_marker(player_position, Color.BLACK, "You");
	}

	/**
	 * Resets chart then sets current game label, budget label and candidate position bias info
	 * @param current_game_num - The game we just started
	 * @param budget - the budget to bias candidate biases
	 * @param max_bias - The limit of the bias distribution
	 */
	public void set_game_info(int current_game_num, int budget, int max_bias) {
		chart.removeAll();
		current_game_change.setText(Integer.toString(current_game_num + 1));
		budget_change.setText(Integer.toString(budget));
		buy_info = ClientGuiInfo.BUY_1 + "between -" + max_bias + " and " + max_bias + " with a greater chance closer to 0";
	}

	/**
	 * Set the budget label for this game
	 * @param new_budget - the budget as an integer
	 */
	public void set_budget(int new_budget) {
		budget_change.setText(Integer.toString(new_budget));
	}

	/**
	 * Sets the updated winnings
	 * @param winnings - Total winnings for all games
	 */
	public void set_winnings(int winnings) {
		winnings_change.setText(Integer.toString(winnings));
	}

	/**
	 * Sets the UI up according to the round given the round number
	 * @param round_num - The current round index in the rounds list
	 */
	public void set_round(int round_num) {
		String round = Constants.LIST_OF_ROUNDS[round_num];
		System.out.println(round);
		current_round_change.setText(round);
		if (round == Constants.FIRST_BUY) {
			set_info_table(Constants.INFO_TABLE_HEADERS, info_table_data);
			set_action_table(Constants.BUY_TABLE_HEADERS, buy_table_data);
			set_info_text(this.buy_info); 
			set_visible_panels(Constants.BUY_ROUND_VISIBILITY);
		} else if (round == Constants.POLL) {
			set_action_table(Constants.VOTE_TABLE_HEADERS, vote_table_data);
			set_info_text(ClientGuiInfo.STRAW);
			set_visible_panels(Constants.VOTE_ROUND_VISIBILITY);
		} else if (round == Constants.PRIMARY) {
			set_action_table(Constants.VOTE_TABLE_HEADERS, vote_table_data);
			set_info_text(ClientGuiInfo.FIRST);
			set_visible_panels(Constants.VOTE_ROUND_VISIBILITY);
		} else if (round == Constants.SECOND_BUY) {
			set_action_table(Constants.BUY_TABLE_HEADERS, buy_table_data);
			set_info_text(ClientGuiInfo.BUY_2);
			set_visible_panels(Constants.BUY_ROUND_VISIBILITY);
		} else if (round == Constants.ELECTION) {
			set_action_table(Constants.VOTE_TABLE_HEADERS, vote_table_data);
			set_info_text(ClientGuiInfo.FINAL);
			set_visible_panels(Constants.VOTE_ROUND_VISIBILITY);
		}
	}

	/**
	 * Allows UI to close and only displays game #s, player number and winnings
	 */
	public void end_game() {
		set_info_text(ClientGuiInfo.FINISHED);
		current_round.setVisible(false);
		set_visible_panels(Constants.END_GAME_VISIBILITY);
		set_default_close();
	}

	/**
	 * Sets the info container
	 * @param text - info for the current round
	 */
	public void set_info_text(String text) {
		info_block.setText(text);
	}

	/**
	 * Given candidate #'s and ideal points, sets them in a chart and tables
	 * @param candidates - array with alternating candidate #s and parties
	 */
	public void add_candidates(int[] candidates, int max_valence) { 
		for (int i=0; i<candidates.length; i+=3) {
			int candidate_number = candidates[i];
			String candidate_viewable = Integer.toString(candidate_number+1);
			int candidate_ideal_pt = candidates[i+1];
			add_marker(candidate_ideal_pt, Color.BLACK, candidate_viewable);
		}
		info_table_data = TableGenerator.generate_info_table(candidates, max_valence);
	}

	/**
	 * Creates a row for each candidate to buy the true value you would receive
	 * @param candidate_nums
	 */
	public void set_buy_table(int[] candidate_nums) {
		buy_table_data = TableGenerator.generate_buy_table(candidate_nums); 
	}

	/**
	 * Sets the action tables with a row for each candidate
 	 */
	public void set_vote_table(int[] candidate_nums) {
		vote_table_data = TableGenerator.generate_vote_table(candidate_nums);
	}

	/**
	 * Adds the vote percentages into the position on the info table
 	 */
	public void add_votes(int position, int[] votes) {
		for (int i=0; i<votes.length; i++) {
			String vote_percentage = votes[i] + "%";
			update_candidate_info(i, position, vote_percentage);
		}
	}

	/**
	 * Updates expected value of candidate position in the info table
	 */
	public void update_candidate_info(int candidate_number, int position, String info) {
		int candidate_viewable = candidate_number + 1;
		for (int i=0; i<info_table_data.length; i++) {
			if (Integer.parseInt(info_table_data[i][0]) == candidate_viewable) {
				info_table_data[i][position] = info;
				set_info_table(Constants.INFO_TABLE_HEADERS, info_table_data);
				return;
			}
		}
	}

	/**
	 * Adds a fixed vertical line to the graph as a marker of the player's ideal point
	 * @param ideal_point - the point on the x axis to put the line
	 */
	private void add_marker(int ideal_point, Color color, String label) {
		marker = new ValueMarker(ideal_point); // Sets the marker at x position ideal_point
		marker.setStroke(new BasicStroke(2));
		marker.setAlpha(1);
		marker.setPaint(color);
		marker.setLabel(label); // Adds a label next to the marker
		marker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		chart.getChart().getXYPlot().addDomainMarker(marker);
		this.all_markers.add(marker);
	}

	/**
	 * Clears the chart of all candidate markers
	 */
	private void remove_all_markers() {
		for (ValueMarker marker : all_markers) {
			chart.getChart().getXYPlot().removeDomainMarker(marker);
		}
		all_markers.clear();
	}

	/**
	 * Given a voter distribution (mean1, std dev1, mean2, std dev2), it generates the data
	 * to graph the distribution and adds the generated data as a line to the chart
	 */
	public void add_voter_data_to_graph(VoterDistribution voter_distribution) {
		double[] voter_data = voter_distribution.get_pdf();

		int dataset_position = 0;
		String dataset_name = "Voters";

		IntervalXYDataset chart_dataset = ChartCreator.create_dataset(voter_data, dataset_name);
		chart.getChart().getXYPlot().setDataset(dataset_position, chart_dataset); 

		Color dataset_color = Color.RED;
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(); 
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, dataset_color);
		chart.getChart().getXYPlot().setRenderer(dataset_position, renderer);
	}

	public void update_expected_payoff(int[] candidate_payoffs, int max_valence, int[] purchases) {
		for (int candidate_num=0; candidate_num<candidate_payoffs.length; candidate_num++) {
			int expected_payoff = candidate_payoffs[candidate_num];
			String expectation_string = Integer.toString(expected_payoff);
			expectation_string += purchases[candidate_num] == 0 ? " +/- " + max_valence : "";
			update_candidate_info(candidate_num, 2, expectation_string);
		}
	}

	public void remove_candidate_from_buy(String candidate_num) {
		for (int i=0; i<buy_table_data.length; i++) {
			if (buy_table_data[i][0].equals(candidate_num)) {
				remove_row_from_table(i);
			}
		}
		set_action_table(Constants.BUY_TABLE_HEADERS, buy_table_data);
	}

	private void remove_row_from_table(int candidate_index) {
		String[][] subtracted_data = new String[buy_table_data.length-1][];
		for (int i=0, j=0; i<buy_table_data.length; i++) {
			if (i != candidate_index) {
				subtracted_data[j] = buy_table_data[i];
				j++;
			}
		}
		buy_table_data = subtracted_data;
	}

	// -------------------------------------- Add panes ----------------------------------------- //

	/**
	 * Add the labels that describe the game
	 */
	private void add_game_label_panel() {
		Label current_game_fixed = new Label("Current Game: ", Label.RIGHT);
		current_game_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		current_game_change = new Label("--");
		current_game_change.setFont(Constants.MEDIUM_LABEL);
		JPanel current_game = new JPanel();
		current_game.add(current_game_fixed);
		current_game.add(current_game_change);

		Label num_games_fixed = new Label("Total Games: ", Label.RIGHT); 
		num_games_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		num_games_change = new Label("--");
		num_games_change.setFont(Constants.MEDIUM_LABEL);
		JPanel num_games = new JPanel();
		num_games.add(num_games_fixed);
		num_games.add(num_games_change);

		Label current_round_fixed = new Label("Current Round: ", Label.RIGHT);
		current_round_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		current_round_change = new Label("--");
		current_round_change.setFont(Constants.MEDIUM_LABEL);
		current_round = new JPanel();
		current_round.add(current_round_fixed);
		current_round.add(current_round_change);

		Label winnings_fixed = new Label("Winnings: ", Label.RIGHT);
		winnings_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		winnings_change = new Label("0");
		winnings_change.setFont(Constants.MEDIUM_LABEL);
		JPanel winnings = new JPanel();
		winnings.add(winnings_fixed);
		winnings.add(winnings_change);

		game_label_panel = new JPanel();
		game_label_panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Game Info",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		game_label_panel.add(current_game);
		game_label_panel.add(num_games);
		game_label_panel.add(current_round);
		game_label_panel.add(winnings);

		content.add(game_label_panel);
	}

	/**
	 * Add the labels that describe the player info
	 */
	private void add_player_label_panel() {

		Label player_number_fixed = new Label("Player Number: ", Label.RIGHT);
		player_number_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		player_number_change = new Label("--");
		player_number_change.setFont(Constants.MEDIUM_LABEL);
		JPanel player_number = new JPanel();
		player_number.add(player_number_fixed);
		player_number.add(player_number_change);

		Label ideal_point_fixed = new Label("Your Position: ", Label.RIGHT);
		ideal_point_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		ideal_point_change = new Label("--");
		ideal_point_change.setFont(Constants.MEDIUM_LABEL);
		JPanel ideal_point = new JPanel();
		ideal_point.add(ideal_point_fixed);
		ideal_point.add(ideal_point_change);

		Label budget_fixed = new Label("Budget: ", Label.RIGHT);
		budget_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		budget_change = new Label("--");
		budget_change.setFont(Constants.MEDIUM_LABEL);
		JPanel budget = new JPanel();
		budget.add(budget_fixed);
		budget.add(budget_change);

		player_label_panel = new JPanel();
		player_label_panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Player Info",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		player_label_panel.add(player_number);
		player_label_panel.add(ideal_point);
		player_label_panel.add(budget);

		content.add(player_label_panel);
	}

	/**
	 * Add the text box that provides round info
	 */
	private void add_info_panel() {
		info_block = new JTextArea(ClientGuiInfo.NOT_STARTED);
		info_block.setEditable(false);
		info_block.setMargin(new Insets(20, 50, 20, 50));
		JPanel info_panel = new JPanel(new GridLayout(1,1));
		info_panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Instructions",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		info_panel.add(info_block);

		content.add(info_panel);
	}

	/**
	 * Add the table with candidate info
	 */
	private JScrollPane add_info_table() {
		info_table = new JTable(null);
		info_table.getTableHeader().setReorderingAllowed(false);
		JScrollPane info_table_scroller = new JScrollPane(info_table);
		info_table_scroller.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Candidate Info",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		return info_table_scroller;
	}

	/**
	 * Sets the info table headers and data
	 */
	private void set_info_table(String[] headers, String[][] data) {
		DefaultTableModel info_table_model = new DefaultTableModel(data, headers) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		info_table.setModel(info_table_model);
	}

	/**
	 * Add the table with buy/vote buttons
	 */
	private JScrollPane add_action_table() {
		action_table = new JTable(null);
		action_table.getTableHeader().setReorderingAllowed(false);
		JScrollPane action_table_scroller = new JScrollPane(action_table);
		action_table_scroller.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Action Table",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		return action_table_scroller;
	}

	/**
	 * Sets the action table headers and data
	 * Sets the last column to have buttons
	 */
	private void set_action_table(final String[] headers, String[][] data) {
		DefaultTableModel action_table_model = new DefaultTableModel(data, headers) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				if ((headers.length-1) == column) {
					return true;
				}
				return false;
			}
		};
		action_table.setModel(action_table_model);

		int button_column = headers.length - 1;
		String column_name = headers[button_column];
		action_table.getColumn(column_name).setCellRenderer(new ButtonRenderer());
		action_table.getColumn(column_name).setCellEditor(new ButtonEditor(pcs, action_table)); 
	}

	/**
	 * Add the chart showing voter distribution, candidate expectations,
	 * and the user ideal point
	 */
	private ChartPanel add_chart() {
		ChartPanel chart_panel = ChartCreator.create_blank_chart();
		return chart_panel;
	}

	/**
	 * Add the button to end buy rounds
	 */
	private void add_end_round_panel() {
		JButton end_round = new JButton("End Round");
		end_round.setPreferredSize(Constants.BIG_BUTTON);
		end_round.setFont(Constants.BIG_LABEL);
		end_round.setBackground(Constants.GREEN);
		end_round.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pcs.firePropertyChange(Integer.toString(Constants.END_ROUND), null, null);
			}
		});
		end_round_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		end_round_panel.add(end_round);
		content.add(end_round_panel);
	}
}
