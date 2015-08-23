package ru.jader.xsdtool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.jader.xsdtool.gui.view.MainView;
import ru.jader.xsdtool.gui.view.View;


public class Application
{
    private static Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws Exception
    {
    	try {
            View mainWindow = new MainView(logger);
            mainWindow.render();
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.log(Level.SEVERE, errors.toString());
        }
    }
}
