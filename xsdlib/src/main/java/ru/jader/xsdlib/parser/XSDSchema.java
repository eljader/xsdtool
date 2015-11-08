package ru.jader.xsdlib.parser;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

public class XSDSchema extends Schema {

    public void load(File file) throws XmlException, IOException {
        this.sts = XmlBeans
            .compileXsd(
                new XmlObject[]{XmlObject.Factory.parse(file)},
                XmlBeans.getBuiltinTypeSystem(),
                null
            )
        ;
        this.stl = XmlBeans
            .typeLoaderUnion(
                new SchemaTypeLoader[]{this.sts, XmlBeans.getBuiltinTypeSystem()}
            )
        ;
    }
}
