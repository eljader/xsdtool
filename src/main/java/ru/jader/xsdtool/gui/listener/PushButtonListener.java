package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.apache.xmlbeans.SchemaComponent;

import ru.jader.xsdtool.common.KeyValue;
import ru.jader.xsdtool.parser.SimpleXsdParser;
import ru.jader.xsdtool.parser.XsdParser;
import ru.jader.xsdtool.parser.handler.Handler;
import ru.jader.xsdtool.parser.handler.XlsResultHandler;

public final class GenerateXlsResultListener implements ActionListener {
	
	private static String EXTENSION = ".xlsx";
	
	private JComboBox<KeyValue<String, SchemaComponent>> schemaCombo;
	
	public GenerateXlsResultListener(JComboBox<KeyValue<String, SchemaComponent>> schemaCombo) {
		this.schemaCombo = schemaCombo;
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		button.setEnabled(false);
		KeyValue<String, SchemaComponent> item = (KeyValue<String, SchemaComponent>) schemaCombo.getSelectedItem();
		OutputStream out = null;
		try {
			out = new FileOutputStream(String.format("%s%s", item.getValue().getName().getLocalPart(), EXTENSION));
			Handler handler = new XlsResultHandler(out);
			XsdParser parser = new SimpleXsdParser(handler);
			parser.parse(item.getValue());

			handler.finalize();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try { out.close(); } catch (IOException e1) { e1.printStackTrace(); }
		}
	
		button.setEnabled(true);
	}
}
