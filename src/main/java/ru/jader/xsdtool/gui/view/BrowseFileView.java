package ru.jader.xsdtool.gui.view;

import java.awt.event.WindowAdapter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ru.jader.xsdtool.gui.listener.act.AcceptFileListener;

public class BrowseFileView extends FrameView {
	
	private static String WINDOW_NAME = "xsdtool";
	private static int WINDOW_WIDTH = 500;
	private static int WINDOW_HEIGHT = 350;
	
	@Override
	protected void doRender(JFrame frame) {
		JPanel panel = getPanel(frame);
		frame.add(panel);
		
		frame.setVisible(true);	
	}

	@Override
	protected WindowAdapter getWindowListner() {
		return null;
	}

	@Override
	protected ViewSettings getViewSettings() {
		return new ViewSettings(WINDOW_NAME, WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	protected JPanel getPanel(JFrame frame) {
		JPanel panel = new JPanel();		
		panel.setLayout(null);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".xsd .wsdl", "xsd", "wsdl");

	    JFileChooser chooser = new JFileChooser();
	    chooser.setBounds(0, 0, frame.getWidth() - 15, frame.getHeight() - 35);
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("implement me!!!");
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    chooser.addChoosableFileFilter(filter);
		
	    chooser.addActionListener(new AcceptFileListener());
	    
	  //  filter.accept(f);
	    
	    panel.add(chooser);
	
		return panel;
	}
}
