package ru.jader.xsdtool.gui.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.XmlException;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import ru.jader.xsdlib.parser.ParseComponentException;
import ru.jader.xsdlib.parser.Schema;
import ru.jader.xsdlib.parser.SchemaComponentParser;
import ru.jader.xsdlib.parser.SimpleSchemaComponentParser;
import ru.jader.xsdlib.parser.WSDLSchema;
import ru.jader.xsdlib.parser.XSDSchema;
import ru.jader.xsdlib.parser.handler.ParseHandler;
import ru.jader.xsdlib.parser.handler.ParseHandlerException;
import ru.jader.xsdlib.parser.handler.XLSDocumentHandler;
import ru.jader.xsdtool.gui.control.features.SearchFilterListener;
import ru.jader.xsdtool.gui.control.features.editor.MergeCellEditor;
import ru.jader.xsdtool.gui.control.features.editor.XSDLabelEditor;
import ru.jader.xsdtool.gui.control.features.span.ComputationSpanHelper;
import ru.jader.xsdtool.gui.document.SpreadsheetSource;
import ru.jader.xsdtool.gui.template.DefaultTemplateManager;
import ru.jader.xsdtool.gui.template.TemplateManager;
import ru.jader.xsdtool.gui.template.TemplateManagerException;
import ru.jader.xsdtool.gui.utils.SchemaComponentConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class MainController {

	@FXML private TextField filepath;
	@FXML private ComboBox<SchemaComponent> schemaCombo;
	@FXML private SpreadsheetView documentEditor;
	@FXML private TextArea output;
	@FXML private MenuItem makeDocumentMenuItem;

	@FXML
	public void initialize() {
		new MergeCellEditor(new ComputationSpanHelper(documentEditor)).initialize(documentEditor);
		new XSDLabelEditor().initialize(documentEditor);
	}

	@FXML protected void makeDocument(ActionEvent event) throws XmlException, IOException, ParseComponentException, ParseHandlerException {
		FileChooser chooser = this.getFileChooser("Generate Document", ".xlsx", "*.xlsx");
	    File file = chooser.showOpenDialog(null);

	    if(file != null) {
	    	Workbook workbook = file.exists() ? new XSSFWorkbook(new FileInputStream(file)) : new XSSFWorkbook();
			SchemaComponent component = schemaCombo.getValue();
			String filename = file.getAbsolutePath();
			OutputStream out = null;

			try {
				out = new FileOutputStream(filename);
				ParseHandler handler = new XLSDocumentHandler(out, new SpreadsheetSource(workbook, documentEditor));
				SchemaComponentParser parser = new SimpleSchemaComponentParser(handler);
				parser.parse(component);

				handler.complete();
			} finally {
				out.close();
			}

			logMessage(String.format("Document %s generated succesful", filename));
	    }
	}

	@FXML protected void loadSchema(ActionEvent event) throws XmlException, IOException {
	    FileChooser chooser = this.getFileChooser("Load Schema", ".xsd .wsdl", "*.xsd", "*.wsdl");
	    File file = chooser.showOpenDialog(null);

	    if(file != null) {

		    filepath.setText(file.getAbsolutePath());

		    Schema schema = this.getSchema(file);
		    ObservableList<SchemaComponent> items = FXCollections.observableArrayList();

	        items.addAll(Arrays.asList(schema.globalElements()));
	        items.addAll(Arrays.asList(schema.globalTypes()));

	        new SearchFilterListener<SchemaComponent>(schemaCombo, items, new SchemaComponentConverter());

	        schemaCombo.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					makeDocumentMenuItem.setDisable(false);
				}
			});

		    logMessage(String.format("%s processed succesful", file.getAbsolutePath()));
	    }
	}

	@FXML protected void loadTemplate() throws TemplateManagerException {
		FileChooser chooser = this.getFileChooser("Load Template", ".xml", "*.xml");
	    File file = chooser.showOpenDialog(null);

	    if(file != null) {
	    	TemplateManager manager = new DefaultTemplateManager(documentEditor, file);
	    	manager.read();

			logMessage(String.format("Template %s load succesful", file.getAbsolutePath()));
	    }

	    throw new TemplateManagerException("test");
	}

	@FXML protected void saveTemplate() throws TemplateManagerException {
		FileChooser chooser = this.getFileChooser("Save Template", ".xml", "*.xml");
	    File file = chooser.showSaveDialog(null);

	    if(file != null) {
	    	TemplateManager manager = new DefaultTemplateManager(documentEditor, file);
	    	manager.write();

			logMessage(String.format("Template %s saved succesful", file.getAbsolutePath()));
	    }
	}

	private FileChooser getFileChooser(String title, String description, String... extensions) {
		FileChooser chooser = new FileChooser();

		chooser.setTitle(title);
	    chooser
	    	.getExtensionFilters()
	    	.add(new FileChooser.ExtensionFilter(description, extensions))
	    ;

	    return chooser;
	}

    private Schema getSchema(File file) throws XmlException, IOException {
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") +1 ).toLowerCase();
        Schema schema = null;

        if(ext.equals("xsd"))
            schema = new XSDSchema();
        if(ext.equals("wsdl"))
            schema = new WSDLSchema();
        if(schema == null)
            throw new RuntimeException(String.format("unsupported file extension .%s", ext));

        schema.load(file);
        return schema;
    }

	public void logMessage(String message) {
		output.appendText(message + System.getProperty("line.separator"));
	}

	public void logError(Throwable e) {
	    StringWriter error = new StringWriter();
	    e.printStackTrace(new PrintWriter(error));

		output.appendText(error.toString() + System.getProperty("line.separator"));
	}
}
