package ru.jader.xsdtool.gui.document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.XmlException;

import ru.jader.xsdlib.parser.ParseComponentException;
import ru.jader.xsdlib.parser.SchemaComponentParser;
import ru.jader.xsdlib.parser.SimpleSchemaComponentParser;
import ru.jader.xsdlib.parser.handler.ParseHandler;
import ru.jader.xsdlib.parser.handler.ParseHandlerException;
import ru.jader.xsdlib.parser.handler.XlsDocumentHandler;
import ru.jader.xsdlib.parser.handler.XlsDocumentHandler.XlsDocumentTemplate;

public final class XlsDocumentGenerator {

	String filename;
	SchemaComponent component;
	XlsDocumentTemplate template;

	public XlsDocumentGenerator(String filename, SchemaComponent component, XlsDocumentTemplate template) {
		this.filename = filename;
		this.component = component;
		this.template = template;
	}

	public void generate() throws XmlException, IOException, ParseComponentException, ParseHandlerException {
		OutputStream out = null;

		try {
			out = new FileOutputStream(filename);
			ParseHandler handler = new XlsDocumentHandler(out, template);
			SchemaComponentParser parser = new SimpleSchemaComponentParser(handler);
			parser.parse(component);

			handler.complete();

		} finally {
			out.close();
		}
	}
}
