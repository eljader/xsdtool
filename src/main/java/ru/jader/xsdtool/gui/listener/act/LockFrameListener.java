package ru.jader.xsdtool.gui.listener.act;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class LockFrameListener implements ActionListener {
	
	JFrame frame;
	
	public LockFrameListener(JFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		frame.setEnabled(false);
	}
}
