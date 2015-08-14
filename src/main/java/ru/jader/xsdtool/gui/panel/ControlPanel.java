package ru.jader.xsdtool.gui.panel;

import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.xmlbeans.SchemaComponent;

import ru.jader.xsdtool.gui.command.GenerateXlsxDocumentCommand;
import ru.jader.xsdtool.gui.command.LoadSchemaFileCommand;
import ru.jader.xsdtool.gui.listener.LoadFileListener;
import ru.jader.xsdtool.gui.listener.PushButtonListener;
import ru.jader.xsdtool.gui.listener.SwitchButtonListener;

public class ControlPanel extends JPanel {

    private static final long serialVersionUID = -3930894157955341754L;
    private int width;
    private Logger logger;

    public ControlPanel(int width, Logger logger) {
        this.width = width;
        this.logger = logger;
        initialize();
    }

    private JComboBox<SchemaComponent> schemaCombo;
    private JButton parseButton;
    private JButton browseFile;
    private JTextField filePath;

    protected void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(width, 100));
        this.add(createAddFilePanel());
        this.add(createMakeTemplatePane());
        this.addActionListeners();
    }

    private JPanel createAddFilePanel() {
        JPanel addFilePane = new JPanel();
        addFilePane.setLayout(new BoxLayout(addFilePane, BoxLayout.X_AXIS));
        addFilePane.setPreferredSize(new Dimension(width, 100));

        filePath = new JTextField();
        filePath.setMaximumSize(new Dimension(width - 220, 25));
        filePath.setEditable(false);

        browseFile = new JButton("Load Schema");
        browseFile.setMaximumSize(new Dimension(150, 25));

        addFilePane.add(filePath);
        addFilePane.add(Box.createRigidArea(new Dimension(20,10)));
        addFilePane.add(browseFile);

        return addFilePane;
    }

    private JPanel createMakeTemplatePane() {
        JPanel makeTemplatePane = new JPanel();
        makeTemplatePane.setLayout(new BoxLayout(makeTemplatePane, BoxLayout.X_AXIS));
        makeTemplatePane.setPreferredSize(new Dimension(width, 100));

        schemaCombo = new JComboBox<SchemaComponent>();
        schemaCombo.setMaximumSize(new Dimension(width - 220, 25));
        schemaCombo.setEditable(false);

        parseButton = new JButton("Make Template");
        parseButton.setMaximumSize(new Dimension(150, 25));
        parseButton.setEnabled(false);

        makeTemplatePane.add(schemaCombo);
        makeTemplatePane.add(Box.createRigidArea(new Dimension(20,10)));
        makeTemplatePane.add(parseButton);

        return makeTemplatePane;
    }

    private void addActionListeners() {
        schemaCombo.addActionListener(
            new SwitchButtonListener(parseButton)
            .setLogger(logger)
        );

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
    }
}
