package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;

import org.apache.xmlbeans.SchemaComponent;

import ru.jader.xsdtool.common.KeyValue;

public final class SwitchButtonListener extends LoggableActionListener {

    private JButton button;

    public SwitchButtonListener(JButton button) {
        this.button = button;
    }

    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent event) {
        try{

            JComboBox<KeyValue<String, SchemaComponent>> schemaCombo =
                (JComboBox<KeyValue<String, SchemaComponent>>) event.getSource()
            ;

            KeyValue<String, SchemaComponent> item =
                (KeyValue<String, SchemaComponent>) schemaCombo.getSelectedItem()
            ;

            button.setEnabled(item == null ? false: true);

        } catch (Exception exception) { logError(exception); }
    }
}
