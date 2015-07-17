package ru.jader.xsdtool.gui.view;

import java.awt.event.WindowAdapter;

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
        schemaCombo.setBounds(frame.getWidth() - 520, frame.getHeight() - 350, 220, 25);
        schemaCombo.setEditable(false);
        schemaCombo.setEditable(false);
        panel.add(schemaCombo);

        JButton parseButton = new JButton("make template");
        parseButton.setBounds(frame.getWidth() - 280, frame.getHeight() - 350, 120, 25);
        parseButton.setEnabled(false);
        panel.add(parseButton);

        schemaCombo.addActionListener(new SwitchButtonListener(parseButton));
        parseButton.addActionListener(new PushButtonListener(new GenerateXlsxDocumentCommand(schemaCombo)));

        JTextField filePath = new JTextField();
        filePath.setBounds(frame.getWidth() - 520, frame.getHeight() - 400, 220, 25);
        filePath.setEditable(false);
        panel.add(filePath);

        JButton browseFile = new JButton("load schema");
        browseFile.setBounds(frame.getWidth() - 280, frame.getHeight() - 400, 120, 25);
        browseFile.addActionListener(new LoadFileListener(
                "Load Schema",
                new FileNameExtensionFilter(".xsd .wsdl", "xsd", "wsdl"),
                new LoadSchemaFileCommand(filePath, schemaCombo)
            )
        );
        panel.add(browseFile);

        int textAreaHeight = frame.getHeight() / 3;
        int textAreaWidth = frame.getWidth() - 20;
        int indentTop = frame.getHeight() - textAreaHeight - 50;

        JTextArea queryTextArea = new JTextArea(500, 200);
        queryTextArea.setBounds(0, indentTop, textAreaWidth, textAreaHeight);
        queryTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(queryTextArea);
        scrollPane.setBounds(0, indentTop, textAreaWidth, textAreaHeight);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane);
        return panel;
    }
}
