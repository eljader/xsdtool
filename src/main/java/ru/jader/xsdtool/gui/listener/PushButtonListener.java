package ru.jader.xsdtool.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import ru.jader.xsdtool.gui.command.Command;

public final class PushButtonListener implements ActionListener {
		
	private Command command;
		
	public PushButtonListener(Command command) {
		this.command = command;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		lockButton(button);
		command.excute();
		unlockButton(button);
	}
	
	private void lockButton(JButton button) {
		button.setEnabled(false);
	}
	
	private void unlockButton(JButton button) {
		button.setEnabled(true);
	}
}
