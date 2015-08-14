package ru.jader.xsdtool.gui.view;

import java.awt.event.WindowAdapter;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ru.jader.xsdtool.gui.panel.ControlPanel;
import ru.jader.xsdtool.gui.panel.HeaderEditorPanel;
import ru.jader.xsdtool.gui.panel.OutputPanel;

public class MainView extends FrameView {

    private Logger logger;

    public MainView(Logger logger) {
        this.logger = logger;
    }

    private static String WINDOW_NAME = "xsdtool";
    private static int WINDOW_WIDTH = 800;
    private static int WINDOW_HEIGHT = 600;

    @Override
    protected void doRender(JFrame frame) {
        JPanel panel = getContentPanel();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    protected WindowAdapter getWindowListner() {
        return null;
    }

    @Override
    protected ViewSettings getViewSettings() {
        return new ViewSettings(WINDOW_NAME, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    protected JPanel getContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new ControlPanel(frame.getWidth(), logger));
        panel.add(new HeaderEditorPanel());
        panel.add(new OutputPanel(logger));
        return panel;
    }
}
