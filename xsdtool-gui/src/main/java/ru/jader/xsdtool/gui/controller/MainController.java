package ru.jader.xsdtool.gui.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;

import ru.jader.xsdlib.parser.ParseComponentException;
import ru.jader.xsdlib.parser.Schema;
import ru.jader.xsdlib.parser.SchemaComponentParser;
import ru.jader.xsdlib.parser.SimpleSchemaComponentParser;
import ru.jader.xsdlib.parser.WsdlSchema;
import ru.jader.xsdlib.parser.XsdSchema;
import ru.jader.xsdlib.parser.handler.ParseHandler;
import ru.jader.xsdlib.parser.handler.ParseHandlerException;
import ru.jader.xsdlib.parser.handler.XlsResultHandler;
import ru.jader.xsdtool.gui.common.control.SearchFilterListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class MainController {

	@FXML private TextField filepath;
	@FXML private ComboBox<SchemaComponent> schemaCombo;
	@FXML private TextArea output;
	@FXML private Button makeTemplateButton;

	@FXML protected void makeTemplate(ActionEvent event) throws XmlException, IOException, ParseComponentException, ParseHandlerException {
		SchemaComponent component = schemaCombo.getValue();
		String filename = String.format("%s%s", component.getName().getLocalPart(), ".xlsx");
		OutputStream out = null;

		try {
			out = new FileOutputStream(String.format("%s%s", component.getName().getLocalPart(), ".xlsx"));
			ParseHandler handler = new XlsResultHandler(out);
			SchemaComponentParser parser = new SimpleSchemaComponentParser(handler);
			parser.parse(component);

			handler.finalize();
			logMessage(String.format("%s generated succesful", filename));

		} finally {
			out.close();
		}
	}

	@FXML protected void loadSchema(ActionEvent event) throws XmlException, IOException {
	    FileChooser chooser = new FileChooser();

	    chooser.setTitle("Load Schema");
	    chooser
	    	.getExtensionFilters()
	    	.add(new FileChooser.ExtensionFilter(".xsd .wsdl", "*.xsd", ".wsdl"))
	    ;

	    File file = chooser.showOpenDialog(new Stage());

	    if(file != null) {

		    filepath.setText(file.getAbsolutePath());

		    Schema schema = getSchema(file);
		    ObservableList<SchemaComponent> items = FXCollections.observableArrayList();

	        items.addAll(Arrays.asList(schema.globalElements()));
	        items.addAll(Arrays.asList(schema.globalTypes()));

	        new SearchFilterListener<SchemaComponent>(schemaCombo, items, new StringConverter<SchemaComponent>() {

				@Override
				public String toString(SchemaComponent object) {
		            if(object instanceof SchemaGlobalElement)
		            	return assebmleText((SchemaComponent) object, "element");
		            if(object instanceof SchemaType)
		            	return assebmleText((SchemaComponent) object, "type");
					return null;
				}

		        private String assebmleText(SchemaComponent component, String type) {
		            return String.format("%s (%s)", component.getName().getLocalPart(), type);
		        }

				@Override
				public SchemaComponent fromString(String string) {
					return null;
				}
			});

	        schemaCombo.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					makeTemplateButton.setDisable(false);
				}
			});

		    logMessage(String.format("%s processed succesful", file.getAbsolutePath()));
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

	private void logMessage(String message) {
		output.appendText(message + System.getProperty("line.separator"));
	}

	private void logError(String message) {
		output.appendText(message + System.getProperty("line.separator"));
	}
}
