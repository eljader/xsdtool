package ru.jader.xsdtool.gui.panel;

import java.awt.BorderLayout;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class OutputPanel extends JPanel {

    private static final long serialVersionUID = 2417755132415164445L;
    private Logger logger;

    public OutputPanel(Logger logger) {
        this.logger = logger;
        initialize();
    }

    protected void initialize() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());

        JTextArea queryTextArea = new JTextArea(1, 1);
        queryTextArea.setEditable(false);
        queryTextArea.setLineWrap(true);
        queryTextArea.setWrapStyleWord(true);
        addLogHandler(queryTextArea);

        JScrollPane scrollPane = new JScrollPane(queryTextArea);
        scrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);
    }

    private void addLogHandler(JTextArea appender) {
        logger.addHandler(new Handler() {

            public void publish(LogRecord logRecord) {
                appender.append(logRecord.getMessage() + System.getProperty("line.separator"));
                appender.setCaretPosition(appender.getText().length());
            }

            public void flush() {}
            public void close() {}
        });
    }
}
