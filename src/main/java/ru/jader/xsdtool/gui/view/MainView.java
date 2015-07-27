package ru.jader.xsdtool.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.xmlbeans.SchemaComponent;

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
        panel.add(getControlPane());
      //  panel.add(getHeaderEditorPane());
        panel.add(getOutputPane());

        return panel;
    }

    private JPanel getControlPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(frame.getWidth(), 100));

        JPanel addFilePane = new JPanel(new GridBagLayout());
        addFilePane.setPreferredSize(new Dimension(frame.getWidth(), 100));
        addFilePane.setBorder(BorderFactory.createLineBorder(Color.black));


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridwidth = GridBagConstraints.BOTH;

        JPanel makeTemplatePane = new JPanel();
        makeTemplatePane.setLayout(new BoxLayout(makeTemplatePane, BoxLayout.X_AXIS));
        makeTemplatePane.setPreferredSize(new Dimension(frame.getWidth(), 100));
        makeTemplatePane.setBorder(BorderFactory.createLineBorder(Color.black));

        JTextField filePath = new JTextField();
        filePath.setMaximumSize(new Dimension(frame.getWidth() - 220, 25));
        filePath.setEditable(false);

        JButton browseFile = new JButton("Load Schema");
        browseFile.setPreferredSize(new Dimension(150, 25));

        JComboBox<SchemaComponent> schemaCombo = new JComboBox<SchemaComponent>();
        schemaCombo.setMaximumSize(new Dimension(frame.getWidth() - 220, 25));
        schemaCombo.setAlignmentY(JComponent.TOP_ALIGNMENT);
        schemaCombo.setEditable(false);

        JButton parseButton = new JButton("Make Template");
        parseButton.setMaximumSize(new Dimension(150, 25));
        parseButton.setAlignmentY(JComponent.TOP_ALIGNMENT);
        parseButton.setEnabled(false);

        schemaCombo.addActionListener(new SwitchButtonListener(parseButton).setLogger(logger));

        parseButton.addActionListener(
            new PushButtonListener(
                new GenerateXlsxDocumentCommand(schemaCombo)
            )
            .setLogger(logger)
        );

        browseFile.addActionListener(
            new LoadFileListener(
                "Load Schema",
                new FileNameExtensionFilter(".xsd .wsdl", "xsd", "wsdl"),
                new LoadSchemaFileCommand(filePath, schemaCombo)
            )
            .setLogger(logger)
        );

        addFilePane.add(filePath, constraints);
        addFilePane.add(browseFile, constraints);

        makeTemplatePane.add(schemaCombo);
        makeTemplatePane.add(Box.createRigidArea(new Dimension(20,10)));
        makeTemplatePane.add(parseButton);

        panel.add(addFilePane);
        panel.add(makeTemplatePane);

        return panel;
    }

    private JPanel getHeaderEditorPane() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel getOutputPane() {
        JPanel panel = new JPanel(new BorderLayout());

        int marginLeft = 5;
        int textAreaHeight = frame.getHeight() / 3;
        int textAreaWidth = frame.getWidth() - (marginLeft * 2);
        int marginTop = frame.getHeight() - textAreaHeight - 50;

        panel.setPreferredSize(new Dimension(textAreaWidth, textAreaHeight));

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
