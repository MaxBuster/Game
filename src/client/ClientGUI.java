package client;

import java.awt.BorderLayout;
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
	
	/**
	 * Initialize the Swing GUI with all the components
	 */
	public ClientGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
		
		this.content = new JPanel(new GridLayout(7, 1, 0, 5));
		this.content.setBorder(new EmptyBorder(100, 100, 100, 100));
		add_game_label_panel();
		add_player_label_panel();
		add_info_panel();
		JScrollPane info_table = add_info_table();
		JScrollPane action_table = add_action_table();
		ChartPanel chart = add_chart(new double[0], 50); // FIXME real dataset
		
		JPanel tables = new JPanel(new GridLayout(2, 1));
		tables.add(info_table);
		tables.add(action_table);
		JPanel all_info = new JPanel(new GridLayout(1, 2));
		all_info.add(tables);
		all_info.add(chart);
		content.add(all_info);
		
		add_end_round_panel();
		setContentPane(this.content);
				
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
	
	public void set_player_number(int[] player_number) {
		// FIXME set player label
	}
	
	public void set_player_info(int[] player_info) {
		// FIXME set player info - ideal_pt, party
	}
	
	public void set_game_info(int[] game_info) {
		// FIXME set game info - candidates, budget, dist, round/game
	}
	
	/**
	 * Add the labels that describe the game
	 */
	private void add_game_label_panel() {
		Label current_game = new Label("Current Game: 0");
		current_game.setFont(Constants.MEDIUM_LABEL);
		Label num_games = new Label("Total Games: x"); // FIXME update
		num_games.setFont(Constants.MEDIUM_LABEL);
		Label current_round = new Label("Current Round: Not Started");
		current_round.setFont(Constants.MEDIUM_LABEL);
		Label winnings = new Label("Winnings: x");
		winnings.setFont(Constants.MEDIUM_LABEL);
		
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
		Label player_number = new Label("Player Number: x");
		player_number.setFont(Constants.MEDIUM_LABEL);
		Label party = new Label("Party: x"); // FIXME update
		party.setFont(Constants.MEDIUM_LABEL);
		Label ideal_point = new Label("Ideal Point: x");
		ideal_point.setFont(Constants.MEDIUM_LABEL);
		Label budget = new Label("Budget: x");
		budget.setFont(Constants.MEDIUM_LABEL);

		JPanel player_label_panel = new JPanel();
		player_label_panel.add(player_number);
		player_label_panel.add(party);
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
		// TODO set font, orientation, color...
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
//		content.add(info_table_scroller);
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
//		content.add(action_table_scroller);
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
	
//		content.add(chart_panel);
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
	}
}
