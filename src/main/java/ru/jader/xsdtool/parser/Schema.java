package ru.jader.xsdtool.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

public class Schema {
	protected SchemaTypeSystem sts;
	protected SchemaTypeLoader stl;
	
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
				new SchemaTypeLoader[]{sts,XmlBeans.getBuiltinTypeSystem()}
			)
		;
	}
	
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
}
