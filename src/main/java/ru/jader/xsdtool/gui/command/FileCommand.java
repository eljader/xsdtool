package ru.jader.xsdtool.gui.command;

import java.io.File;

public abstract class FileCommand implements Command{
	
	protected File file;

	public File getFile() {
		return file;
	}

	public FileCommand setFile(File file) {
		this.file = file;
		return this;
	}
}
