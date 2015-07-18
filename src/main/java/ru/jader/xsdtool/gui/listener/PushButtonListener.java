package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import javax.swing.JButton;

import ru.jader.xsdtool.gui.command.Command;

public final class PushButtonListener extends LoggableActionListener {
		
	private Command command;
		
	public PushButtonListener(Command command) {
		this.command = command;
	}
	
	public void actionPerformed(ActionEvent event) {
		try {
			
			JButton button = (JButton) event.getSource();
			
			lockButton(button);
			command.excute();
			unlockButton(button);
			
		} catch (Exception exception) { logError(exception); }
	}
	
	private void lockButton(JButton button) {
		button.setEnabled(false);
	}
	
	private void unlockButton(JButton button) {
		button.setEnabled(true);
	}
}
