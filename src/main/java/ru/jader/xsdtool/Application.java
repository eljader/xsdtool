package ru.jader.xsdtool;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JTextArea;

import ru.jader.xsdtool.gui.view.MainView;
import ru.jader.xsdtool.gui.view.View;


public class Application
{
    private static Logger log = Logger.getLogger(Application.class.getName());
    private static JTextArea appender;

    public static void main( String[] args ) throws Exception
    {
        try {
            appender = new JTextArea();
            View mainWindow = new MainView(appender);
            mainWindow.render();

            log.addHandler(new Handler() {

                public void publish(LogRecord logRecord) {
                    appender.append(logRecord.getMessage() + "\n");
                    appender.setCaretPosition(appender.getText().length());
                }

                public void flush() {}
                public void close() {}
            });

        } catch (Exception e) {
            log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
    }
}
