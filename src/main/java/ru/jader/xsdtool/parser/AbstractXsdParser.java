package ru.jader.xsdtool.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

public abstract class AbstractXsdParser {

	protected SchemaTypeSystem sts;
	protected SchemaTypeLoader stl;

	public AbstractXsdParser(File file) throws XmlException, IOException {	    
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
	
	public void parseElement(SchemaLocalElement element, String path) {
		SchemaType type = element.getType();
		boolean isNotRecursive = !this.isRecursive(path, element);
		path = this.rebuildPath(path, element);

		if(type.getContentModel() != null && isNotRecursive)
			parseType(type, path); 
		else
			this.process(path, element);
	}

	public void parseType(SchemaType type, String path) {
		SchemaProperty[] attributes = type.getAttributeProperties();

		for (int i = 0; i < attributes.length; i++) {
			SchemaProperty attribute = attributes[i];
			this.process(path, attribute);
		}

		SchemaParticle[] childes = type.getContentModel().getParticleChildren();

		if(childes == null)
			childes = type.getContentModel().getType().getContentModel().getParticleChildren();

		for (int i = 0; i < childes.length; i++) {
			SchemaLocalElement element = (SchemaLocalElement) childes[i];
			parseElement(element, path);
		}
	}
	
	public SchemaGlobalElement findElement(QName name) {
		return stl.findElement(name);
	}
	
	public SchemaType findType(QName name) {
		return stl.findType(name);
	}
	
	protected abstract String rebuildPath(String path, SchemaLocalElement element);
	
	protected abstract void process(String path, Object parseObject);
	
	protected abstract boolean isRecursive(String path, SchemaLocalElement element);
}
