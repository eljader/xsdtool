package ru.jader.xsdtool.parser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class XmlResultHandler implements Handler {
	
	private String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	private String ROOT_NAME = "units";
	private String UNIT_NAME = "unit";
	
	private int IDENT_NO = 0;
	private int IDENT_TAB = 4;
	
	private PrintWriter writer;
		
	public XmlResultHandler(String filename, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
		this.writer = new PrintWriter(filename, encoding);
		writer.println(XML_DECLARATION);
		openTag(ROOT_NAME, IDENT_NO);
	}
	
	public void handle(XsdUnit unit) {
		openTag(UNIT_NAME, IDENT_TAB);
		
		addElement("path", unit.getXpath(), IDENT_TAB * 2);
		addElement("type", unit.getType(), IDENT_TAB * 2);
		addElement("description", String.format("<![CDATA[%s]]>", unit.getDescription()), IDENT_TAB * 2);
		
		closeTag(UNIT_NAME, IDENT_TAB);
	}
	
	public void close() {
		closeTag(ROOT_NAME, IDENT_NO);
		writer.close();
	}
	
	private void openTag(String tag, int indentSize) {
		writer.println(String.format("%s<%s>", getIdent(indentSize), tag));
	}
	
	private void closeTag(String tag, int indentSize) {
		writer.println(String.format("%s</%s>", getIdent(indentSize), tag));
	}
	
	private void addElement(String tag, String value, int indentSize) {
		openTag(tag, indentSize);
		writer.println(String.format("%s%s", getIdent(indentSize + IDENT_TAB), value));	
		closeTag(tag, indentSize);
	}
	
	private String getIdent(int size) {
		String ident = "";
		
		for(int i=0; i < size; i++)
			ident += " ";
			
		return ident;
	}
}
