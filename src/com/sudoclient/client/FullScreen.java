package com.sudoclient.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * User: deprecated
 * Date: 4/28/12
 * Time: 1:34 AM
 */
public class FullScreen extends JFrame implements WindowListener {
    private DisplayMode dispModeOld = null;
    private GraphicsDevice device;
    private JFrame source;

    public FullScreen(GraphicsDevice device) {
        setUndecorated(true);
        this.device = device;
        dispModeOld = device.getDisplayMode();
    }

    public void startFullScreen(JFrame source) {
        device.setFullScreenWindow(FullScreen.this);
        setContentPane(source.getContentPane());
        source.setVisible(false);
        this.setVisible(true);
        this.source = source;
    }

    public DisplayMode getDispModeOld() {
        return dispModeOld;
    }

    public void exit() {
        windowDeactivated(null);
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
        source.setContentPane(this.getContentPane());
        source.setSize(800, 650);
        source.setLocationRelativeTo(null);
        source.setVisible(true);
        this.setVisible(false);
    }
}
