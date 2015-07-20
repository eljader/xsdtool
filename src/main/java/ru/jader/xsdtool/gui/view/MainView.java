package ru.jader.xsdtool.gui.view;

import java.awt.event.WindowAdapter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.xmlbeans.SchemaComponent;

import ru.jader.xsdtool.common.KeyValue;
import ru.jader.xsdtool.gui.command.GenerateXlsxDocumentCommand;
import ru.jader.xsdtool.gui.command.LoadSchemaFileCommand;
import ru.jader.xsdtool.gui.listener.PushButtonListener;
import ru.jader.xsdtool.gui.listener.LoadFileListener;
import ru.jader.xsdtool.gui.listener.SwitchButtonListener;

public class MainView extends FrameView {

    private static Logger logger = Logger.getLogger(MainView.class.getName());

    private static String WINDOW_NAME = "xsdtool";
    private static int WINDOW_WIDTH = 800;
    private static int WINDOW_HEIGHT = 600;

    @Override
    protected void doRender(JFrame frame) {
        JPanel panel = getPanel(frame);
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

    protected JPanel getPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JComboBox<KeyValue<String, SchemaComponent>> schemaCombo = new JComboBox<KeyValue<String, SchemaComponent>>();
        schemaCombo.setBounds(20, 60, frame.getWidth() - 220, 25);
        schemaCombo.setEditable(false);
        schemaCombo.setEditable(false);
        panel.add(schemaCombo);

        JButton parseButton = new JButton("Make Template");
        parseButton.setBounds(frame.getWidth() - 180, 60, 150, 25);
        parseButton.setEnabled(false);
        panel.add(parseButton);

        schemaCombo.addActionListener(new SwitchButtonListener(parseButton).setLogger(logger));
        parseButton
            .addActionListener(
                new PushButtonListener(
                    new GenerateXlsxDocumentCommand(schemaCombo)
                )
                .setLogger(logger)
            );

        JTextField filePath = new JTextField();
        filePath.setBounds(20, 10, frame.getWidth() - 220, 25);
        filePath.setEditable(false);
        panel.add(filePath);

        JButton browseFile = new JButton("Load Schema");
        browseFile.setBounds(frame.getWidth() - 180, 10, 150, 25);
        browseFile
            .addActionListener(
                new LoadFileListener(
                    "Load Schema",
                    new FileNameExtensionFilter(".xsd .wsdl", "xsd", "wsdl"),
                    new LoadSchemaFileCommand(filePath, schemaCombo)
                )
                .setLogger(logger)
            )
        ;

        panel.add(browseFile);

        int marginLeft = 5;
        int textAreaHeight = frame.getHeight() / 3;
        int textAreaWidth = frame.getWidth() - (marginLeft * 2);
        int marginTop = frame.getHeight() - textAreaHeight - 50;

        JTextArea queryTextArea = new JTextArea(1, 1);
        queryTextArea.setBounds(marginLeft, marginTop, textAreaWidth, textAreaHeight);
        queryTextArea.setEditable(false);
        queryTextArea.setLineWrap(true);
        queryTextArea.setWrapStyleWord(true);
        addLogHandler(queryTextArea);

        JScrollPane scrollPane = new JScrollPane(queryTextArea);
        scrollPane.setBounds(marginLeft, marginTop, textAreaWidth, textAreaHeight);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane);
        return panel;
    }

    private void addLogHandler(JTextArea appender) {
        logger.addHandler(new Handler() {

            public void publish(LogRecord logRecord) {
                appender.append(logRecord.getMessage() + "\n");
                appender.setCaretPosition(appender.getText().length());
            }

            public void flush() {}
            public void close() {}
        });
    }
}
