package ru.jader.xsdtool.gui.listener.act;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.jader.xsdtool.gui.view.View;

public class RenderViewListener implements ActionListener {
		
	private View view;
	
	public RenderViewListener(View view) {
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {		
		view.render();
	}	
}
