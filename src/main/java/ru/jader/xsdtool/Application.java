package ru.jader.xsdtool;

import ru.jader.xsdtool.gui.view.MainView;
import ru.jader.xsdtool.gui.view.View;


public class Application
{
    public static void main( String[] args ) throws Exception
    {
        View mainWindow = new MainView();
        mainWindow.render();
    }
}
