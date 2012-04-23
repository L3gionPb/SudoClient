package com.sudoclient.client;

import com.sudoclient.widgets.Widgets;

import javax.swing.*;
import java.awt.*;

/**
 * User: deprecated
 * Date: 4/21/12
 * Time: 5:56 PM
 */

public class Client extends JFrame {
    private Widgets widgets;
    private boolean fullscreen;
    private GraphicsDevice device;
    private DisplayMode dispModeOld = null;

    public Client() {
        super("SudoClient");
        fullscreen = false;
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        setSize(800, 650);
        setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (widgets != null) {
            widgets.close();
        }
        System.exit(0);
    }

    public void initComponents() {
        widgets = new Widgets(this);
        add(widgets.getTabPanel(), BorderLayout.NORTH);
        add(widgets, BorderLayout.CENTER);
        pack();
    }

    public void setFullscreen(boolean fullscreen) {
        if (this.fullscreen != fullscreen && device.isFullScreenSupported()) {
            this.fullscreen = fullscreen;

            if (!fullscreen) {
                device.setDisplayMode(dispModeOld);
                setVisible(false);
                dispose();
                setUndecorated(false);
                device.setFullScreenWindow(null);
                setSize(800, 600);
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                dispModeOld = device.getDisplayMode();
                setVisible(false);
                dispose();
                setUndecorated(true);
                device.setFullScreenWindow(this);
                setVisible(true);
            }

            repaint();
        }
    }
}
