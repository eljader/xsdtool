package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.apache.xmlbeans.SchemaComponent;

import ru.jader.xsdtool.common.KeyValue;

public final class SwitchButtonListener implements ActionListener {
	
	private JButton button;
	
	public SwitchButtonListener(JButton button) {
		this.button = button;
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		JComboBox<KeyValue<String, SchemaComponent>> schemaCombo = (JComboBox<KeyValue<String, SchemaComponent>>) e.getSource();
		KeyValue<String, SchemaComponent> item = (KeyValue<String, SchemaComponent>) schemaCombo.getSelectedItem();
		
		button.setEnabled(item.getValue() == null ? false: true);
	}
}
