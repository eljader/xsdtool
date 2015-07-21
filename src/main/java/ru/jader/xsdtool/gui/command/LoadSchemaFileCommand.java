package ru.jader.xsdtool.gui.command;

import java.io.File;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
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
import ru.jader.xsdtool.parser.Schema;
import ru.jader.xsdtool.parser.WsdlSchema;
import ru.jader.xsdtool.parser.XsdSchema;

public final class LoadSchemaFileCommand extends FileCommand {

    private JComboBox<SchemaComponent> schemaCombo;
    private JTextField viewPath;
    private AutoCompleteSupport<SchemaComponent> autocomplete;

    public LoadSchemaFileCommand(
        JTextField viewPath,
        JComboBox<SchemaComponent> schemaCombo
    ) {
        this.schemaCombo = schemaCombo;
        this.viewPath = viewPath;
    }

    @Override
    public void excute() throws CommandException {
        try {
            Schema schema = getSchema(file);
            viewPath.setText(file.getPath());
            schemaCombo.setEditable(true);
            List<SchemaComponent> items = new ArrayList<SchemaComponent>();

            items.addAll(Arrays.asList(schema.globalElements()));
            items.addAll(Arrays.asList(schema.globalTypes()));

            if(autocomplete != null) autocomplete.uninstall();

            autocomplete = AutoCompleteSupport.install(
                schemaCombo,
                GlazedLists.eventListOf(items.toArray(new SchemaComponent[items.size()])),
                GlazedLists.textFilterator(SchemaComponent.class, "Name"),
                new SchemaComponentFormat()
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

    private class SchemaComponentFormat extends Format {
        private static final long serialVersionUID = 2122226791526331666L;
        @Override
        public StringBuffer format(Object object, StringBuffer appender, FieldPosition pos) {
            if(object instanceof SchemaGlobalElement)
                appender.append(assebmleText((SchemaComponent) object, "element"));
            if(object instanceof SchemaType)
                appender.append(assebmleText((SchemaComponent) object, "type"));

            return appender;
        }

        private String assebmleText(SchemaComponent component, String type) {
            return String.format("%s (%s)", component.getName().getLocalPart(), type);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) { return null; }
    }
}
