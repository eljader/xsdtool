package ru.jader.xsdtool.gui.template;

public class TemplateManagerException extends Exception {
	private static final long serialVersionUID = -124113082401673358L;

	public TemplateManagerException(Exception e) {
		super(e);
	}

	public TemplateManagerException(String message) {
		super(message);
	}
}
