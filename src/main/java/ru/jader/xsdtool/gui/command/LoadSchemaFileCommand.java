package ru.jader.xsdtool.gui.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ru.jader.xsdtool.common.KeyValue;
import ru.jader.xsdtool.parser.Schema;
import ru.jader.xsdtool.parser.WsdlSchema;
import ru.jader.xsdtool.parser.XsdSchema;

public final class LoadSchemaFileCommand extends FileCommand {

    private JComboBox<KeyValue<String, SchemaComponent>> schemaList;
    private JTextField viewPath;
    private AutoCompleteSupport<Object> autocomplete;

    public LoadSchemaFileCommand(
        JTextField viewPath,
        JComboBox<KeyValue<String, SchemaComponent>> schemaList
    ) {
        this.schemaList = schemaList;
        this.viewPath = viewPath;
    }

    @Override
    public void excute() throws CommandException {
        try {
            Schema schema = getSchema(file);
            viewPath.setText(file.getPath());
            schemaList.setEditable(true);

            SchemaGlobalElement[] elements = schema.globalElements();
            SchemaType[] types = schema.globalTypes();
            List<KeyValue<String, SchemaComponent>> itemList =
                    new ArrayList<KeyValue<String, SchemaComponent>>();

            for(SchemaGlobalElement element: elements)
                itemList.add(assembleItem(element, "element"));
            for(SchemaType type: types)
                itemList.add(assembleItem(type, "type"));

            if(autocomplete != null) autocomplete.uninstall();

            autocomplete = AutoCompleteSupport.install(
                schemaList,
                GlazedLists.eventListOf(itemList.toArray())
            );

            autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    private Schema getSchema(File file) throws XmlException, IOException {
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

    private KeyValue<String, SchemaComponent>assembleItem(SchemaComponent component, String prefix) {
        return
            new KeyValue<String, SchemaComponent>(
                String.format("%s :%s", prefix, component.getName().getLocalPart()),
                component
            )
        ;
    }
}
