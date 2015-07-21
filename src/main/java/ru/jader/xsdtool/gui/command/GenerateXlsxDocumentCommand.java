package ru.jader.xsdtool.gui.command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JComboBox;

import org.apache.xmlbeans.SchemaComponent;

import ru.jader.xsdtool.parser.SimpleSchemaComponentParser;
import ru.jader.xsdtool.parser.SchemaComponentParser;
import ru.jader.xsdtool.parser.handler.ParseHandler;
import ru.jader.xsdtool.parser.handler.XlsResultHandler;

public final class GenerateXlsxDocumentCommand implements Command {

    private static String EXTENSION = ".xlsx";
    private JComboBox<SchemaComponent> schemaCombo;

    public GenerateXlsxDocumentCommand(JComboBox<SchemaComponent> schemaCombo) {
        this.schemaCombo = schemaCombo;
    }

    @Override
    public void excute() throws CommandException {
        SchemaComponent component = getComponent();
        OutputStream out = null;
        try {
            out = new FileOutputStream(String.format("%s%s", component.getName().getLocalPart(), EXTENSION));
            ParseHandler handler = new XlsResultHandler(out);
            SchemaComponentParser parser = new SimpleSchemaComponentParser(handler);
            parser.parse(component);

            handler.finalize();
        } catch (Exception e) {
            throw new CommandException(e);
        } finally {
            try { out.close(); } catch (IOException e) { throw new CommandException(e); }
        }
    }

    private SchemaComponent getComponent() {
        return
            (SchemaComponent) schemaCombo.getSelectedItem()
        ;
    }
}
