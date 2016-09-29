package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.ui.TextAnchor;

import model.Constants;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = 1L; // Default serial id
	private PropertyChangeSupport pcs;
	private JPanel content;
	
	private Label current_game_change;
	private Label num_games_change;
	private Label current_round_change;
	private Label winnings_change;
	
	private Label player_number_change;
	private Label player_party_change;
	private Label ideal_point_change;
	private Label budget_change;
	
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
		JScrollPane info_table = add_info_table();
		JScrollPane action_table = add_action_table();
		ChartPanel chart = add_chart(new double[0], 50); // FIXME real dataset
		
		JPanel tables = new JPanel();
		tables.setLayout(new BoxLayout(tables, BoxLayout.Y_AXIS));
		tables.add(info_table);
		tables.add(action_table);
		JPanel all_info = new JPanel();
		all_info.setLayout(new BoxLayout(all_info, BoxLayout.X_AXIS));
		all_info.add(tables);
		all_info.add(chart);
		content.add(all_info);
		
		add_end_round_panel();
		setContentPane(this.content);
				
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
	
	public void set_player_number(int[] player_number) {
		player_number_change.setText(Integer.toString(player_number[0])); 
	}
	
	public void set_player_info(int[] player_info) {
		ideal_point_change.setText(Integer.toString(player_info[0]));
		player_party_change.setText(Integer.toString(player_info[1]));
		// FIXME add budget
	}
	
	public void set_game_info(int[] game_info) {
		// FIXME set game info - candidates, budget, dist, round/game
	}
	
	/**
	 * Add the labels that describe the game
	 */
	private void add_game_label_panel() {
		Label current_game_fixed = new Label("Current Game: ", Label.RIGHT);
		current_game_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		current_game_change = new Label("0");
		current_game_change.setFont(Constants.MEDIUM_LABEL);
		JPanel current_game = new JPanel();
		current_game.add(current_game_fixed);
		current_game.add(current_game_change);
		
		Label num_games_fixed = new Label("Total Games: ", Label.RIGHT); 
		num_games_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		num_games_change = new Label("X");
		num_games_change.setFont(Constants.MEDIUM_LABEL);
		JPanel num_games = new JPanel();
		num_games.add(num_games_fixed);
		num_games.add(num_games_change);
		
		Label current_round_fixed = new Label("Current Round: ", Label.RIGHT);
		current_round_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		current_round_change = new Label("Not Started");
		current_round_change.setFont(Constants.MEDIUM_LABEL);
		JPanel current_round = new JPanel();
		current_round.add(current_round_fixed);
		current_round.add(current_round_change);
		
		Label winnings_fixed = new Label("Winnings: ", Label.RIGHT);
		winnings_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		winnings_change = new Label("X");
		winnings_change.setFont(Constants.MEDIUM_LABEL);
		JPanel winnings = new JPanel();
		winnings.add(winnings_fixed);
		winnings.add(winnings_change);
		
		JPanel game_label_panel = new JPanel();
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
		player_number_change = new Label("0");
		player_number_change.setFont(Constants.MEDIUM_LABEL);
		JPanel player_number = new JPanel();
		player_number.add(player_number_fixed);
		player_number.add(player_number_change);
		
		Label player_party_fixed = new Label("Party: ", Label.RIGHT);
		player_party_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		player_party_change = new Label("X");
		player_party_change.setFont(Constants.MEDIUM_LABEL);
		JPanel player_party = new JPanel();
		player_party.add(player_party_fixed);
		player_party.add(player_party_change);
		
		Label ideal_point_fixed = new Label("Ideal Point: ", Label.RIGHT);
		ideal_point_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		ideal_point_change = new Label("X");
		ideal_point_change.setFont(Constants.MEDIUM_LABEL);
		JPanel ideal_point = new JPanel();
		ideal_point.add(ideal_point_fixed);
		ideal_point.add(ideal_point_change);
		
		Label budget_fixed = new Label("Budget: ", Label.RIGHT);
		budget_fixed.setFont(Constants.MEDIUM_BOLD_LABEL);
		budget_change = new Label("X");
		budget_change.setFont(Constants.MEDIUM_LABEL);
		JPanel budget = new JPanel();
		budget.add(budget_fixed);
		budget.add(budget_change);

		JPanel player_label_panel = new JPanel();
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
		JTextArea info_block = new JTextArea();
		info_block.setEditable(false);
		info_block.setText("Game Info"); // FIXME change
		info_block.setMargin(new Insets(20, 50, 20, 50));
		JPanel info_panel = new JPanel(new GridLayout(1,1));
		info_panel.add(info_block);
		
		content.add(info_panel);
	}
	
	/**
	 * Add the table with candidate info
	 */
	private JScrollPane add_info_table() {
		String[] column_names = new String[]{"Candidate #", "Party", "Best Guess", "Straw Votes", "First Round Votes"};
		Object[][] data = new Object[1][5];
		DefaultTableModel info_table_model = new DefaultTableModel(data, column_names) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		JTable info_table = new JTable(info_table_model);
		JScrollPane info_table_scroller = new JScrollPane(info_table);
		info_table_scroller.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
                "Info Table",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		return info_table_scroller;
	}
	
	/**
	 * Add the table with buy/vote buttons
	 */
	private JScrollPane add_action_table() {
		final String[] column_names = new String[]{"Candidate #", "Action"};
		Object[][] data = new Object[1][2];
		DefaultTableModel action_table_model = new DefaultTableModel(data, column_names) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				if (column_names.length == 3 && column == 2) {
					return true;
				} else if (column_names.length == 2 && column == 1) {
					return true;
				}
				return false;
			}
		};
		JTable action_table = new JTable(action_table_model);
		JScrollPane action_table_scroller = new JScrollPane(action_table);
		action_table_scroller.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
                "Action Table",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		return action_table_scroller;
	}

	/**
	 * Add the chart showing voter distribution, candidate expectations,
	 * and the user ideal point
	 */
	private ChartPanel add_chart(double[] chartData, int idealPoint) {
		ChartPanel chart_panel = ChartCreator.create_chart(chartData);

		ValueMarker marker = new ValueMarker(idealPoint);
		marker.setPaint(Color.black);
		marker.setLabel("You");
		marker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		chart_panel.getChart().getXYPlot().addDomainMarker(marker);
	
		return chart_panel;
	}

//	public void addDataset(int candidate, IntervalXYDataset data) {
//		graph.getXYPlot().setDataset(candidate+1, data);
//
//		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(); 
//
//		renderer.setSeriesShapesVisible(0, false);
//		Color[] colors = new Color[]{new Color(0, 0 ,153), new Color(0, 153, 0), new Color(255, 102, 0), 
//				new Color(255, 255, 0), new Color(153, 0, 153), new Color(255,0,204), 
//				new Color(255, 204, 204), new Color(0, 255, 255), new Color(102,0,51), 
//				new Color(139,69,19)};
//		renderer.setSeriesPaint(0, colors[candidate]);
//		graph.getXYPlot().setRenderer(candidate+1, renderer); 
//	}
	
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
				// TODO hide button, fire event
			}
		});
		JPanel end_round_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		end_round_panel.add(end_round);
		content.add(end_round_panel);
	}
	
	// --------------------------------- Custom Table Buttons --------------------------- //
	
	/**
	 * Button renderer that creates a button that can be put in a table cell
	 */
	class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	/**
	 * Button editor which gives the table buttons actions
	 */
	class ButtonEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		protected JButton button;
		private String label;
		private boolean isPushed;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (label.equals("Buy")) { // FIXME don't hardcode
						fireBuy();
					} else if (label.equals("Vote")) {
						fireVote();
					}
					fireEditingStopped();
				}
			});
		}

		public void fireBuy() {
//			int selectedRow = table2.getSelectedRow();
//			int price = (Integer) table2.getModel().getValueAt(selectedRow, 1);
//			if (budget >= price) {
//				int selectedCand = (Integer) table2.getModel().getValueAt(selectedRow, 0) - 1;
//				pcs.firePropertyChange("Buy Info", selectedCand, true); // Same party
//				budget -= price;
//				lblBudget.setText("Budget: " + budget);
//			} else {
//				JOptionPane.showMessageDialog(null, "You don't have enough money to buy that info.");
//			}
		}

		public void fireVote() {
//			int selectedRow = table2.getSelectedRow();
//			int selectedCand = (Integer) table2.getModel().getValueAt(selectedRow, 0) - 1;
//			setTextPane(WAITING_MESSAGE);
//			contentPane.remove(scrollPane2);
//			contentPane.remove(scrollPane1);
//			contentPane.revalidate();
//			contentPane.repaint();
//			pcs.firePropertyChange("Vote", selectedCand, null);
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			label = (value == null) ? "" : value.toString();
			button.setText(label);
			isPushed = true;
			return button;
		}

		public Object getCellEditorValue() {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (isPushed) {
						isPushed = false;
					}
				}
			});
			return new String(label);
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}
	
	/**
	 * Main method to test ui on its own
	 */
	public static void main(String[] args) {
		ClientGUI gui = new ClientGUI(new PropertyChangeSupport(new Object()));
		gui.set_player_number(new int[]{5});
		gui.set_player_info(new int[]{25, 0});
	}
}
