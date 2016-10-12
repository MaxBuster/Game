package client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;

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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.TextAnchor;

import utils.ButtonEditor;
import utils.ButtonRenderer;
import utils.Constants;
import utils.Info;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = 1L; // Default serial id
	private PropertyChangeSupport pcs;
	private JPanel content; // Panel that holds all of the UI
	
	// All game labels
	private Label current_game_change;
	private Label num_games_change;
	private Label current_round_change;
	private Label winnings_change;
	
	// All player labels
	private Label player_number_change;
	private Label player_party_change;
	private Label ideal_point_change;
	private Label budget_change;
	
	// Info text
	private JTextArea info_block;
	
	// All containers
	private JPanel game_label_panel;
	private JPanel player_label_panel;
	private JPanel tables;
	private JPanel all_info;
	private JTable info_table;
	private JScrollPane info_table_pane;
	private JTable action_table;
	private JScrollPane action_table_pane;
	private ChartPanel chart;
	private JPanel end_round_panel;
	
	// Table data
	String[][] info_table_data;
	String[][] buy_table_data;
	String[][] vote_table_data;
	
	/**
	 * Initialize the Swing GUI with all the components
	 */
	public ClientGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
		
		this.content = new JPanel();
		this.content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		this.content.setBorder(new EmptyBorder(100, 100, 100, 100));
		add_game_label_panel();
		add_player_label_panel();
		add_info_panel();
		
		info_table_pane = add_info_table();
		action_table_pane = add_action_table();
		chart = add_chart();
		
		tables = new JPanel();
		tables.setLayout(new BoxLayout(tables, BoxLayout.Y_AXIS));
		tables.add(info_table_pane);
		tables.add(action_table_pane);
		all_info = new JPanel();
		all_info.setLayout(new BoxLayout(all_info, BoxLayout.X_AXIS));
		all_info.add(tables);
		all_info.add(chart);
		content.add(all_info);
		
		add_end_round_panel();
		setContentPane(this.content);
				
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		set_visible_panels(Constants.START_GAME_VISIBILITY);
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
	
	// -------------------------------------- Setters ----------------------------------------- //
	
	public void set_start_info(int[] start_info) {
		player_number_change.setText(Integer.toString(start_info[0])); 
		num_games_change.setText(Integer.toString(start_info[1]));
	}
	
	public void set_player_info(int[] player_info) {
		String party_name = (player_info[0] == Constants.PARTY_1) ? Constants.PARTY_1_NAME : Constants.Party_2_NAME;
		player_party_change.setText(party_name);
		int ideal_point = player_info[1];
		ideal_point_change.setText(Integer.toString(ideal_point));
		add_marker(ideal_point);
	}
	
	public void set_game_info(int[] game_info) {
		current_game_change.setText(Integer.toString(game_info[0]));
		budget_change.setText(Integer.toString(game_info[1]));
	}
	
	public void set_budget(int new_budget) {
		budget_change.setText(Integer.toString(new_budget));
	}
	
	public void set_winnings(int[] winnings) {
		winnings_change.setText(Integer.toString(winnings[0]));
	}
	
	public void set_round(int[] round_message) {
		String round = Constants.LIST_OF_ROUNDS[round_message[0]];
		System.out.println(round);
		current_round_change.setText(round);
		if (round == Constants.FIRST_BUY) {
			set_info_table(Constants.INFO_TABLE_HEADERS, info_table_data);
			set_action_table(Constants.BUY_TABLE_HEADERS, buy_table_data);
			set_info_text(Info.BUY_1);
			set_visible_panels(Constants.BUY_ROUND_VISIBILITY);
		} else if (round == Constants.STRAW_VOTE) {
			set_action_table(Constants.VOTE_TABLE_HEADERS, vote_table_data);
			set_info_text(Info.STRAW);
			set_visible_panels(Constants.VOTE_ROUND_VISIBILITY);
		} else if (round == Constants.FIRST_VOTE) {
			set_action_table(Constants.VOTE_TABLE_HEADERS, vote_table_data);
			set_info_text(Info.FIRST);
			set_visible_panels(Constants.VOTE_ROUND_VISIBILITY);
		} else if (round == Constants.SECOND_BUY) {
			set_action_table(Constants.BUY_TABLE_HEADERS, buy_table_data);
			set_info_text(Info.BUY_2);
			set_visible_panels(Constants.BUY_ROUND_VISIBILITY);
		} else if (round == Constants.FINAL_VOTE) {
			set_action_table(Constants.VOTE_TABLE_HEADERS, vote_table_data);
			set_info_text(Info.FINAL);
			set_visible_panels(Constants.VOTE_ROUND_VISIBILITY);
		}
	}
	
	private void set_info_text(String text) {
		info_block.setText(text);
	}
	
	/**
	 * Given candidate #'s and parties, sets them in a chart and tables
	 * @param candidates - array with alternating candidate #s and parties
	 */
	public void add_candidates(int[] candidates, int party) {
		for (int i=0; i<candidates.length; i+=2) {
			int candidate_number = candidates[i];
			add_candidate_data(Constants.ZERO_TOKENS, candidate_number);
		}
		info_table_data = TableGenerator.generate_info_table(candidates);
		buy_table_data = TableGenerator.generate_buy_table(candidates, party);
		vote_table_data = TableGenerator.generate_vote_table(candidates); 
	}
	
	public void add_votes(int[] votes) {
		int round_num = votes[votes.length-1];
		String round = Constants.LIST_OF_ROUNDS[round_num];
		int position;
		if (round == Constants.STRAW_VOTE) {
			position = 3;
		} else if (round == Constants.FIRST_VOTE) {
			position = 4;
		} else if (round == Constants.FINAL_VOTE) {
			position = 5;
		} else {
			return;
		}
		for (int i=0; i<votes.length-1; i+=2) {
			int candidate_number = votes[i];
			int vote_percentage = votes[i+1];
			update_candidate_info(candidate_number, position, vote_percentage + "%");
		}
	}
	
	/**
	 * Updates info in the candidate table
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
	private void add_marker(int ideal_point) {
		ValueMarker marker = new ValueMarker(ideal_point); // Sets the marker at x position ideal_point
		marker.setPaint(Color.BLACK);
		marker.setLabel("You"); // Adds a label next to the marker
		marker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		chart.getChart().getXYPlot().addDomainMarker(marker);
	}
	
	/**
	 * Given a voter distribution (mean1, std dev1, mean2, std dev2), it generates the data
	 * to graph the distribution and adds the generated data as a line to the chart
	 */
	public void add_voter_data(int[] voter_dist) {
		int dataset_position = 0;
		String dataset_name = "Voters";
		double[] voter_data = VoterDistributionGenerator.generate_data(voter_dist);
		IntervalXYDataset chart_dataset = ChartCreator.create_dataset(voter_data, dataset_name);
		chart.getChart().getXYPlot().setDataset(dataset_position, chart_dataset); 

		Color dataset_color = Color.RED;
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(); 
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, dataset_color);
		chart.getChart().getXYPlot().setRenderer(dataset_position, renderer);
	}

	/**
	 * Given candidate beta information, this generates the data to show the candidate expectations on 
	 * the graph and adds that data as a line on the graph
	 */
	public void add_candidate_data(int[] candidate_tokens, int candidate_number) {
		int dataset_position = candidate_number + 1; 
		String dataset_name = "Candidate " + dataset_position;
		double[] candidate_data = CandidateDistributionGenerator.generate_data(candidate_tokens);
		IntervalXYDataset chart_dataset = ChartCreator.create_dataset(candidate_data, dataset_name);
		chart.getChart().getXYPlot().setDataset(dataset_position, chart_dataset); 

		Color dataset_color = Constants.GRAPH_GOLORS[candidate_number]; // FIXME pick from a list
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(); 
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, dataset_color);
		chart.getChart().getXYPlot().setRenderer(dataset_position, renderer);
	}
	
	public void update_candidate_expected_point(int[] candidate_info) {
		int expected_value = ((candidate_info[0]+1)*100)/(candidate_info[1]+2);
		update_candidate_info(candidate_info[2], 2, Integer.toString(expected_value));
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
		JPanel current_round = new JPanel();
		current_round.add(current_round_fixed);
		current_round.add(current_round_change);
		
		Label winnings_fixed = new Label("Winnings: ", Label.RIGHT);
		winnings_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		winnings_change = new Label("--");
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
		
		Label player_party_fixed = new Label("Party: ", Label.RIGHT);
		player_party_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		player_party_change = new Label("--");
		player_party_change.setFont(Constants.MEDIUM_LABEL);
		JPanel player_party = new JPanel();
		player_party.add(player_party_fixed);
		player_party.add(player_party_change);
		
		Label ideal_point_fixed = new Label("Ideal Point: ", Label.RIGHT);
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
		player_label_panel.add(player_party);
		player_label_panel.add(ideal_point);
		player_label_panel.add(budget);
		
		content.add(player_label_panel);
	}
	
	/**
	 * Add the text box that provides round info
	 */
	private void add_info_panel() {
		info_block = new JTextArea(Info.NOT_STARTED);
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
	
	// -------------------------------------- Testing ----------------------------------------- //
	
	/**
	 * Main method to test ui on its own
	 */
	public static void main(String[] args) {
		ClientGUI gui = new ClientGUI(new PropertyChangeSupport(new Object()));
		gui.set_visible_panels(Constants.BUY_ROUND_VISIBILITY);
		gui.set_start_info(new int[]{5, 2}); // Player 5, 2 games
		gui.set_player_info(new int[]{2, 23}); // Party 2, Ideal Point 23
		gui.set_game_info(new int[]{1, 80, 3}); // Game 1, 80 Budget, 3 Candidates
		gui.add_voter_data(new int[]{40, 5, 80, 5});
		gui.add_candidates(new int[]{0, 1, 1, 2}, 2); // 2 candidates
		gui.add_candidate_data(new int[]{3, 2}, 1);
		gui.update_candidate_info(0, 4, "50%");
		sleep(3000);
	}
	
	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
