package ru.jader.xsdtool;

import java.io.File;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;

import ru.jader.xsdtool.parser.AbstractXsdParser;
import ru.jader.xsdtool.parser.SimpleXsdParser;
import ru.jader.xsdtool.parser.handler.Handler;
import ru.jader.xsdtool.parser.handler.XlsResultHandler;


public class Application
{
    public static void main( String[] args ) throws Exception
    {	
    	File file = new File("");
    	//Handler handler = new XmlResultHandler("", "UTF-8");
    	Handler handler = new XlsResultHandler("");
    	AbstractXsdParser parser = new SimpleXsdParser(file, handler);
    	
    	
    	SchemaGlobalElement element = parser
            .findElement(
                new QName("", "")
            )
        ;
        
    	SchemaType type = element.getType();
        
        /*
    	SchemaType type = parser
            .findType(
                new QName("", "")
            )
        ;*/

        parser.parseType(type, new String());
        
        handler.close();
    }
}
