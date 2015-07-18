package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LoggableActionListener implements ActionListener {
	
	private Logger logger;
	
	public ActionListener setLogger(Logger logger) {
		this.logger = logger;
		return this;
	}
	
	protected void logError(Exception e) {
		if(logger != null) {
			logger.log(Level.SEVERE, assembleError(e));
		} else {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	private String assembleError(Exception e) {
    	StringWriter errors = new StringWriter();
    	e.printStackTrace(new PrintWriter(errors));	
		return errors.toString();
	}
}
