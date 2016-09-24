package server;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.Constants;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L; // Default serial version ID
	private PropertyChangeSupport pcs;
	private JPanel content;
	
	private JTable player_table;
	private String[] player_table_column_names;
	private Object[][] player_table_data;
	
	/**
	 * TODO:
	 * Update table on player removed/added
	 * 
	 * Style:
	 * Change text, buttons background style
	 * Change spacing so it doesn't fit width
	 */
	
	public ServerGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
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
	
	private void add_game_label_panel() {
		Label current_game = new Label("Current Game: 0", Label.CENTER);
		Label num_games = new Label("Number of Gamers: x", Label.CENTER); // FIXME get from model
		Label current_round = new Label("Current Round: Not Started", Label.CENTER);
		
		JPanel game_label_panel = new JPanel(new GridLayout(1, 3));
		game_label_panel.add(current_game);
		game_label_panel.add(num_games);
		game_label_panel.add(current_round);
		
		content.add(game_label_panel);
	}
	
	private void add_game_controls_panel() {
		final Button start_game = new Button(Constants.START_GAME);
		final Button end_game = new Button(Constants.END_GAME);
		Button write_data = new Button(Constants.WRITE_DATA);
		
		final JPanel game_controls_panel = new JPanel(new GridLayout(1, 3));
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
	
	private void add_players_table() {
		DefaultTableModel table_model = new DefaultTableModel(player_table_data, player_table_column_names) {
			private static final long serialVersionUID = 1L; // Default serial version id

			public boolean isCellEditable(int row, int column)
			{
				if (column == 1) {
					return true;
				} 
				return false;
			}
		};
		player_table = new JTable(table_model);
		player_table.getColumn("Remove").setCellRenderer(new ButtonRenderer()); // FIXME change to constant
		player_table.getColumn("Remove").setCellEditor(new ButtonEditor(new JCheckBox()));
		Dimension d = new Dimension(5, 5);
		player_table.setPreferredScrollableViewportSize(d);
		JScrollPane scroll_table = new JScrollPane(player_table);
		content.add(scroll_table);
	}
	
	// -------------------------------- Custom Table -------------------------------------------- //
	
	private class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = 1L; // Default serial version id

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
			setText((value == null) ? "" : value.toString()); // FIXME what is this
			return this;
		}
	}

	private class ButtonEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L; // Default serial version id
		protected JButton button;
		private String label;
		private boolean isPushed;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = player_table.getSelectedRow();
					int playerNumber = (Integer) player_table.getModel().getValueAt(selectedRow, 0);
					fireEditingStopped();
					pcs.firePropertyChange("Remove Player", null, playerNumber); // FIXME change to constant
				}
			});
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
}
