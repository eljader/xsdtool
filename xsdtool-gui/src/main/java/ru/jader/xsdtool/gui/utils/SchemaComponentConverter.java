package ru.jader.xsdtool.gui.utils;

import javafx.util.StringConverter;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;

public class SchemaComponentConverter extends StringConverter<SchemaComponent> {

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

	public SchemaComponent fromString(String string) {
		return null;
	}
}
