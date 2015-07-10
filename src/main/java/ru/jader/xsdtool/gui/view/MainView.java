package ru.jader.xsdtool.gui.view;

import java.awt.event.WindowAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import ru.jader.xsdtool.gui.listener.act.LockFrameListener;
import ru.jader.xsdtool.gui.listener.act.RenderViewListener;
import ru.jader.xsdtool.gui.listener.win.UnlockFrameCloseListener;

public class MainView extends FrameView {
	
	private static String WINDOW_NAME = "xsdtool";
	private static int WINDOW_WIDTH = 800;
	private static int WINDOW_HEIGHT = 600;

	@Override
	protected void doRender(JFrame frame) {
		JPanel panel = getPanel(frame);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
				
		JButton runButton = new JButton("make template");
		runButton.setBounds(frame.getWidth() - 150, frame.getHeight() - 400, 120, 25);
		runButton.setEnabled(false);
		panel.add(runButton);
		
		JButton browseFile = new JButton("load schema");
		browseFile.setBounds(frame.getWidth() - 280, frame.getHeight() - 400, 120, 25);
		panel.add(browseFile);
		
		FrameView browseFileView = new BrowseFileView();
		
		browseFileView.getFrame().addWindowListener(new UnlockFrameCloseListener(frame));
		browseFile.addActionListener(new RenderViewListener(browseFileView));
		browseFile.addActionListener(new LockFrameListener(frame));
				
		int textAreaHeight = frame.getHeight() / 3;
		int textAreaWidth = frame.getWidth() - 20;
		int indentTop = frame.getHeight() - textAreaHeight - 50;
		
		JTextArea queryTextArea = new JTextArea(500, 200);
		queryTextArea.setBounds(0, indentTop, textAreaWidth, textAreaHeight);
		queryTextArea.setEditable(false);
					
		JScrollPane scrollPane = new JScrollPane(queryTextArea);
		scrollPane.setBounds(0, indentTop, textAreaWidth, textAreaHeight);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollPane);		
		return panel;
	}
}
