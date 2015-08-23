package ru.jader.xsdtool.gui.panel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

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

        model.addColumn("");
        model.addColumn("");
        model.addColumn("");
        model.addRow(new Object[]{"", "", ""});
        model.addRow(new Object[]{"", "", ""});
        model.addRow(new Object[]{"", "", ""});

        return model;
    }

}
