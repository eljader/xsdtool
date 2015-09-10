package ru.jader.xsdlib.parser.handler;

import java.io.PrintWriter;

import ru.jader.xsdlib.parser.model.XsdUnit;

public final class XmlResultHandler implements ParseHandler {

    private String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private String ROOT_NAME = "units";
    private String UNIT_NAME = "unit";

    private int IDENT_NO = 0;
    private int IDENT_TAB = 4;

    private PrintWriter writer;

    public XmlResultHandler(PrintWriter writer) {
        this.writer = writer;
        this.writer.println(XML_DECLARATION);
        openTag(ROOT_NAME, IDENT_NO);
    }

    public void handle(XsdUnit unit) {
        openTag(UNIT_NAME, IDENT_TAB);

        addElement("path", unit.getPath(), IDENT_TAB * 2);
        addElement("type", unit.getType(), IDENT_TAB * 2);
        addElement("description", String.format("<![CDATA[%s]]>", unit.getDescription()), IDENT_TAB * 2);

        closeTag(UNIT_NAME, IDENT_TAB);
    }

    public void finalize() {
        closeTag(ROOT_NAME, IDENT_NO);
        writer.flush();
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
