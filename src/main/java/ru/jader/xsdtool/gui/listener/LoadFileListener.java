package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import ru.jader.xsdtool.gui.command.FileCommand;

public final class LoadFileListener implements ActionListener {
	
	private FileFilter filter;
	private String title;
	private FileCommand command;
	
	public LoadFileListener(String title, FileFilter filter, FileCommand command) {
		this.filter = filter;
		this.title = title;
		this.command = command;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(this.title);
	    chooser.setAcceptAllFileFilterUsed(false);
	    chooser.addChoosableFileFilter(this.filter);

	    if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
	    	command
	    		.setFile(chooser.getSelectedFile())
	    		.excute()
	    ;
	}
}
