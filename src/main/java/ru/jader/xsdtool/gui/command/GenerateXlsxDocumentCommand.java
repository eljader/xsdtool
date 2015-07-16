package ru.jader.xsdtool.gui.command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JComboBox;

import org.apache.xmlbeans.SchemaComponent;

import ru.jader.xsdtool.common.KeyValue;
import ru.jader.xsdtool.parser.SimpleSchemaComponentParser;
import ru.jader.xsdtool.parser.SchemaComponentParser;
import ru.jader.xsdtool.parser.handler.Handler;
import ru.jader.xsdtool.parser.handler.XlsResultHandler;

public final class GenerateXlsxDocumentCommand implements Command {

	private static String EXTENSION = ".xlsx";
	
	private JComboBox<KeyValue<String, SchemaComponent>> schemaCombo;
	
	public GenerateXlsxDocumentCommand(JComboBox<KeyValue<String, SchemaComponent>> schemaCombo) {
		this.schemaCombo = schemaCombo;
	}

	@Override
	public void excute() {
		SchemaComponent component = getComponent();
		OutputStream out = null;
		try {
			out = new FileOutputStream(String.format("%s%s", component.getName().getLocalPart(), EXTENSION));
			Handler handler = new XlsResultHandler(out);
			SchemaComponentParser parser = new SimpleSchemaComponentParser(handler);
			parser.parse(component);

			handler.finalize();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { out.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	@SuppressWarnings("unchecked")
	private SchemaComponent getComponent() {
		return 
			((KeyValue<String, SchemaComponent>) schemaCombo.getSelectedItem()).getValue()
		;
	}
}
