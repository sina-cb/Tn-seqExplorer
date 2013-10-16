package CustomGUIComponent;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class BoldCellRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object aValue,
			boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
		Component component = super.getTableCellRendererComponent(table, aValue, isSelected, hasFocus, rowIndex, columnIndex);
		//component.setFont(component.getFont().deriveFont(Font.BOLD));
		component.setFont(new Font("Arial" , Font.BOLD, 11));
		return component;
	}
}
