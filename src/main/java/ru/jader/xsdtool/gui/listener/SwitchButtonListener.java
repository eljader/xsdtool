package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;

import org.apache.xmlbeans.SchemaComponent;

public final class SwitchButtonListener extends LoggableActionListener {

    private JButton button;

    public SwitchButtonListener(JButton button) {
        this.button = button;
    }

    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent event) {
        try{
            JComboBox<SchemaComponent> schemaCombo =  (JComboBox<SchemaComponent>) event.getSource();
            SchemaComponent item = (SchemaComponent) schemaCombo.getSelectedItem();

            button.setEnabled(item == null ? false: true);
        } catch (Exception exception) { logError(exception); }
    }
}
