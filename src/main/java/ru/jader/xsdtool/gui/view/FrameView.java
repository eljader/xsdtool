package ru.jader.xsdtool.gui.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

public abstract class FrameView implements View {

    protected JFrame frame;

    public void render() {
        doRender(getFrame());
    }

    public JFrame getFrame() {
        return frame == null ? createFrame() : frame;
    }

    protected JFrame createFrame() {
        ViewSettings settings = getViewSettings();

        this.frame = new JFrame(settings.getName());
        frame.setSize(settings.getWidth(), settings.getHeight());

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);

        frame.setLocation(x, y);

        if (getWindowListner() != null)
            frame.addWindowListener(getWindowListner());

        return frame;
    }

    protected class ViewSettings {
        private String name;
        private int width;
        private int height;

        public ViewSettings(String name, int width, int height) {
            this.name = name;
            this.width = width;
            this.height = height;
        }

        public String getName() {
            return name;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    protected abstract void doRender(JFrame frame);

    protected abstract WindowAdapter getWindowListner();

    protected abstract ViewSettings getViewSettings();
}
