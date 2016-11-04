package utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class ButtonEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L; // Default version id
	private static final JCheckBox CHECK_BOX = new JCheckBox();

	private PropertyChangeSupport pcs;
	private JTable table;
	protected JButton button;
	private String label;
	private boolean isPushed;

	public ButtonEditor(PropertyChangeSupport pcs, JTable table) {
		super(CHECK_BOX);
		this.pcs = pcs;
		this.table = table;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
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
		if (isPushed) {
			int selected_row = table.getSelectedRow();
			Object first = table.getModel().getValueAt(selected_row, 0);
			Object second = table.getModel().getValueAt(selected_row, 1);
			pcs.firePropertyChange(label, first, null); // FIXME first and second can't be the same 
		}
		isPushed = false;
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
