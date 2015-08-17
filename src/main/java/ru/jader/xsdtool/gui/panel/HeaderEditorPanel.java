package ru.jader.xsdtool.gui.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class HeaderEditorPanel extends JPanel {

    private static final long serialVersionUID = -1374844715315119216L;

    public HeaderEditorPanel () {
        initialize();
    }

    protected void initialize() {
        JTable table = new JTable(getTableModel());

        table.setCellSelectionEnabled(true);

        HeaderEditorPopupPanel popup = new HeaderEditorPopupPanel(table);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEtchedBorder());

        table.addMouseListener(popup.getListener());

        this.add(table);
        this.add(popup);
    }

    private AbstractTableModel getTableModel() {
        DefaultTableModel model = new DefaultTableModel();

        model.setValueAt("test", 0, 0);
        model.setValueAt("test1", 1, 0);
        model.setValueAt("test2", 0, 1);
        model.setValueAt("test3", 1, 1);
        model.setValueAt("test3", 2, 1);
        model.setValueAt("test2", 0, 2);

        return model;
    }

    class DefaultTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -1358300499280932689L;
        private List<String> columnNames = new ArrayList<String>();
        private ArrayList<List<Object>> data = new ArrayList<List<Object>>();

        public int getColumnCount() {
            return columnNames.size();
        }

        public int getRowCount() {
            int max = 0;
            for(List<Object> col : data)
                if(col.size() > max)
                    max = col.size();

            return max;
        }

        public String getColumnName(int col) {
            return columnNames.get(col);
        }

        public Object getValueAt(int row, int col) {
            try {
                return data.get(col).get(row);
            } catch (IndexOutOfBoundsException e) {
                return new String();
            }
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return true;
        }

        public void removeRow(int row) {
            for(List<Object> col : data) {
                try {
                    col.remove(row);
                } catch (IndexOutOfBoundsException e) {}
            }

            fireTableRowsDeleted(row, row);
        }

        public void removeColumn(int col) {
          try {
                columnNames.remove(col);
                data.remove(col);
          } catch (IndexOutOfBoundsException e) {}
        }

        public void setValueAt(Object value, int row, int col) {
            try {
                data.get(col);
            } catch (IndexOutOfBoundsException e) {
                 columnNames.add(col, value.toString());
                 data.add(col, new ArrayList<Object>());
            }

            data.get(col).add(row, value);
            fireTableCellUpdated(row, col);
        }
    }
}
