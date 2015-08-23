package ru.jader.xsdtool.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

public class HeaderEditorPopupPanel extends JPanel {

    private static final long serialVersionUID = -7089899688708290054L;
    public JPopupMenu popup;
    private JComponent component;

    public HeaderEditorPopupPanel(JTable component) {
        popup = new JPopupMenu();
        this.component = component;

        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JTable table = (JTable) component;
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                if(event.getActionCommand().equals("delete row")) {
                    model.removeRow(table.getSelectedRow());
                }

                if(event.getActionCommand().equals("delete col")) {
                    table.removeColumn(table.getColumnModel().getColumn(table.getSelectedColumn()));
                }

                if(event.getActionCommand().equals("add row")) {
                	Object[] row = new Object[model.getColumnCount()];
                	for(int cell = 0; cell < model.getColumnCount(); cell++)
                		row[cell] = "";

                	model.addRow(row);
                }

                if(event.getActionCommand().equals("add col")) {
                	model.addColumn("");
                }

                if(event.getActionCommand().equals("combine")) {

                }

                if(event.getActionCommand().equals("split")) {

                }

                System.out.println("action: " + event.getActionCommand());
            }
        };

        JMenuItem item;
        popup.add(item = new JMenuItem("combine"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("split"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("add row"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("add col"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("delete row"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("delete col"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);

        popup.setLabel("Justification");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        popup.addPopupMenuListener(new PopupPrintListener());
    }

    public MouseAdapter getListener() {
        return (MouseAdapter) new MousePopupListener();
    }

    class MousePopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseClicked(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(component, e.getX(), e.getY());
            }
        }
    }

    class PopupPrintListener implements PopupMenuListener {
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            System.out.println("visible");
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            System.out.println("invisible");
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
            System.out.println("hidden");
        }
    }
}
