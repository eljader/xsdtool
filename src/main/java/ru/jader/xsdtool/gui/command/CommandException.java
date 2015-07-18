package ru.jader.xsdtool.gui.command;

public class CommandException extends Exception {

	private static final long serialVersionUID = 8550088932421254697L;
	
	public CommandException(Exception e) {
		super(e);
	}
}
