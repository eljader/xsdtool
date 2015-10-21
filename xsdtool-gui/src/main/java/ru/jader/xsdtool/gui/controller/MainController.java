package ru.jader.xsdtool.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.XmlException;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ru.jader.xsdlib.parser.ParseComponentException;
import ru.jader.xsdlib.parser.Schema;
import ru.jader.xsdlib.parser.WsdlSchema;
import ru.jader.xsdlib.parser.XsdSchema;
import ru.jader.xsdlib.parser.handler.ParseHandlerException;
import ru.jader.xsdlib.parser.handler.XlsDocumentHandler.XlsDocumentTemplate;
import ru.jader.xsdtool.gui.control.features.SearchFilterListener;
import ru.jader.xsdtool.gui.control.features.editor.MergeCellEditor;
import ru.jader.xsdtool.gui.control.features.editor.SpreadsheetEditor;
import ru.jader.xsdtool.gui.control.features.editor.XsdLabelEditor;
import ru.jader.xsdtool.gui.document.SpreadsheetXlsDocumentTemplate;
import ru.jader.xsdtool.gui.document.XlsDocumentGenerator;
import ru.jader.xsdtool.gui.utils.SchemaComponentConverter;
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

public class MainController {

	@FXML private TextField filepath;
	@FXML private ComboBox<SchemaComponent> schemaCombo;
	@FXML private SpreadsheetView documentEditor;
	@FXML private TextArea output;
	@FXML private Button makeDocumentButton;

	private SpreadsheetEditor mergeCellEditor;
	private SpreadsheetEditor xsdLabelEditor;

	@FXML
	public void initialize() {
		this.mergeCellEditor = new MergeCellEditor().initialize(documentEditor);
		this.xsdLabelEditor = new XsdLabelEditor().initialize(documentEditor);
	}

	@FXML protected void makeDocument(ActionEvent event) throws XmlException, IOException, ParseComponentException, ParseHandlerException {
		SchemaComponent component = schemaCombo.getValue();
		String filename = String.format("%s%s", component.getName().getLocalPart(), ".xlsx");

		XlsDocumentTemplate template = new SpreadsheetXlsDocumentTemplate(
			documentEditor,
			mergeCellEditor,
			xsdLabelEditor
		);

		XlsDocumentGenerator generator = new XlsDocumentGenerator(filename, component, template);
		generator.generate();

		logMessage(String.format("%s generated succesful", filename));
	}

	@FXML protected void loadSchema(ActionEvent event) throws XmlException, IOException {
	    FileChooser chooser = new FileChooser();

	    chooser.setTitle("Load Schema");
	    chooser
	    	.getExtensionFilters()
	    	.add(new FileChooser.ExtensionFilter(".xsd .wsdl", "*.xsd", "*.wsdl"))
	    ;

	    File file = chooser.showOpenDialog(new Stage());

	    if(file != null) {

		    filepath.setText(file.getAbsolutePath());

		    Schema schema = getSchema(file);
		    ObservableList<SchemaComponent> items = FXCollections.observableArrayList();

	        items.addAll(Arrays.asList(schema.globalElements()));
	        items.addAll(Arrays.asList(schema.globalTypes()));

	        new SearchFilterListener<SchemaComponent>(schemaCombo, items, new SchemaComponentConverter());

	        schemaCombo.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					makeDocumentButton.setDisable(false);
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
