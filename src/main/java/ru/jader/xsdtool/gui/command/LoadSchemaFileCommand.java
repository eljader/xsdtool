package ru.jader.xsdtool.gui.command;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;

import ru.jader.xsdtool.common.KeyValue;
import ru.jader.xsdtool.parser.Schema;

public final class LoadSchemaFileCommand extends FileCommand {

	private JComboBox<KeyValue<String, SchemaComponent>> schemaList;
	private JTextField filePath;
	private Schema schema;
	
	public LoadSchemaFileCommand(JTextField filePath, JComboBox<KeyValue<String, SchemaComponent>> schemaList, Schema schema) {
		this.schemaList = schemaList;
		this.filePath = filePath;
		this.schema = schema;
	}

	@Override
	public void excute() {
		try {
			schema.load(file);
			filePath.setText(file.getPath());
			schemaList.removeAllItems();
			
			SchemaGlobalElement[] elements = schema.globalElements();
			SchemaType[] types = schema.globalTypes();
				
			schemaList.addItem(new KeyValue<String, SchemaComponent>("", null));
			
			for(SchemaGlobalElement element: elements)
				schemaList.addItem(new KeyValue<String, SchemaComponent>(
					"element: " + element.getName().getLocalPart(), 
					element
				)
			);
			
			for(SchemaType type: types)
				schemaList.addItem(new KeyValue<String, SchemaComponent>(
					"type: " + type.getName().getLocalPart(), 
					type
				)
			);
			
			schemaList.setEditable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
