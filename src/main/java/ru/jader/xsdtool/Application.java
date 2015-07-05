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
    	File file = new File("xsd/human_factor/Customers.xsd");
    	XmlResultHandler handler = new XmlResultHandler("xsd/createOrUpdateCustomerRequest.xml", "UTF-8");
    	DefaultXsdParser parser = new DefaultXsdParser(file, handler);
    	
	    SchemaGlobalElement element = parser
    		.getSchemaTypeLoader()
    		.findElement(
				new QName("http://www.neoflex.ru/Customers/", "createOrUpdateCustomerRequest")
			)
    	;
	    
        parser.processElement(element, new String());
        handler.close();
    }
}