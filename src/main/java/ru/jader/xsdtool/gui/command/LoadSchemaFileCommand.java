package ru.jader.xsdtool.gui.command;

import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;

import ru.jader.xsdtool.common.KeyValue;
import ru.jader.xsdtool.parser.Schema;
import ru.jader.xsdtool.parser.WsdlSchema;
import ru.jader.xsdtool.parser.XsdSchema;

public final class LoadSchemaFileCommand extends FileCommand {

    private JComboBox<KeyValue<String, SchemaComponent>> schemaList;
    private JTextField viewPath;

    public LoadSchemaFileCommand(
        JTextField viewPath,
        JComboBox<KeyValue<String, SchemaComponent>> schemaList
    ) {
        this.schemaList = schemaList;
        this.viewPath = viewPath;
    }

    @Override
    public void excute() {
        try {
            Schema schema = getSchema(file);
            viewPath.setText(file.getPath());
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


    public Schema getSchema(File file) throws XmlException, IOException {
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") +1 ).toLowerCase();
        Schema schema = null;

        if(ext.equals("xsd"))
            schema = new XsdSchema();
        if(ext.equals("wsdl"))
            schema = new WsdlSchema();
        if(schema == null)
            throw new RuntimeException(String.format("unsupported file extension .%s", ext));

        schema.load(file);
        return schema;
    }
}
