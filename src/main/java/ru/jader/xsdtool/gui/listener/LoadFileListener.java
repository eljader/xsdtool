package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import ru.jader.xsdtool.gui.command.FileCommand;

public final class LoadFileListener extends LoggableActionListener {

    private FileFilter filter;
    private String title;
    private FileCommand command;

    public LoadFileListener(String title, FileFilter filter, FileCommand command) {
        this.filter = filter;
        this.title = title;
        this.command = command;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {

            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle(this.title);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(this.filter);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                command
                    .setFile(file)
                    .excute()
                ;

                logMessage(String.format("%s processed succesful", file.getAbsolutePath()));
            }

        } catch (Exception exception) { logError(exception); }
    }
}
