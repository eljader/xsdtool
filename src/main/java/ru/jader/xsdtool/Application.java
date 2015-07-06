package ru.jader.xsdtool;

import java.io.File;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;

import ru.jader.xsdtool.parser.XmlResultHandler;
import ru.jader.xsdtool.parser.DefaultXsdParser;

public class Application 
{
    public static void main( String[] args ) throws Exception
    {	
    	File file = new File("Some.xsd");
    	XmlResultHandler handler = new XmlResultHandler("Some.xml", "UTF-8");
    	DefaultXsdParser parser = new DefaultXsdParser(file, handler);
    	
    	SchemaGlobalElement element = parser
            .getSchemaTypeLoader()
            .findElement(
                new QName("SomeNS", "SomeRequest")
            )
        ;
	    
        parser.processElement(element, new String());
        handler.close();
    }
}
