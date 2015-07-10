package ru.jader.xsdtool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;

import ru.jader.xsdtool.gui.view.MainView;
import ru.jader.xsdtool.gui.view.View;
import ru.jader.xsdtool.parser.AbstractXsdParser;
import ru.jader.xsdtool.parser.SimpleXsdParser;
import ru.jader.xsdtool.parser.handler.Handler;
import ru.jader.xsdtool.parser.handler.XlsResultHandler;


public class Application
{
    public static void main( String[] args ) throws Exception
    {	
    	String needle= "getCustomerRequest";
    	
    	File file = new File("");
    	//Handler handler = new XmlResultHandler("", "UTF-8");
    	OutputStream out= new FileOutputStream("xsd/result/" + needle + ".xlsx");
    	Handler handler = new XlsResultHandler(out);
    	AbstractXsdParser parser = new SimpleXsdParser(file, handler);
    		
    	SchemaGlobalElement element = parser
            .findElement(
                new QName("", needle)
            )
        ;
            	
    	SchemaType type = element.getType();
        
        /*
    	SchemaType type = parser
            .findType(
                new QName("", "")
            )
        ;*/

        //parser.parseType(type, new String());
        
        handler.finalize();
        out.close();
        
        
        View mainWindow = new MainView();
        mainWindow.render();
    }
}
