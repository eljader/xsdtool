package ru.jader.xsdtool.gui.listener.win;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class UnlockFrameCloseListener extends WindowAdapter{
	
	JFrame frame;
	
	public UnlockFrameCloseListener(JFrame frame) {
		this.frame = frame;
	}
	
	public void windowClosing(WindowEvent e) {
		frame.setEnabled(true);
	}
}
