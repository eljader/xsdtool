package ru.jader.xsdtool;

import java.io.File;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;

import ru.jader.xsdtool.parser.Handler;
import ru.jader.xsdtool.parser.XlsResultHandler;
import ru.jader.xsdtool.parser.DefaultXsdParser;

public class Application
{
    public static void main( String[] args ) throws Exception
    {	
    	File file = new File("");
    	//Handler handler = new XmlResultHandler("", "UTF-8");
    	Handler handler = new XlsResultHandler("");
    	DefaultXsdParser parser = new DefaultXsdParser(file, handler);
    	
    	SchemaGlobalElement element = parser
            .getSchemaTypeLoader()
            .findElement(
                new QName("", "")
            )
        ;
	    
        parser.processElement(element, new String());
        handler.close();
    }
}
