package ru.jader.xsdlib.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlException;

public abstract class Schema {

    protected SchemaTypeSystem sts;
    protected SchemaTypeLoader stl;

    public SchemaGlobalElement findElement(QName name) {
        return stl.findElement(name);
    }

    public SchemaType findType(QName name) {
        return stl.findType(name);
    }

    public SchemaType[] globalTypes() {
        return sts.globalTypes();
    }

    public SchemaGlobalElement[] globalElements() {
        return sts.globalElements();
    }

    public abstract void load(File file) throws XmlException, IOException;
}
