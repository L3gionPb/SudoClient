package com.sudoclient.client;

import com.sudoclient.widgets.WidgetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * User: deprecated
 * Date: 4/21/12
 * Time: 5:56 PM
 */

public class Client extends JFrame implements WindowListener {
    private WidgetManager widgetManager;
    private boolean fullscreen;
    private GraphicsDevice device;
    private DisplayMode dispModeOld = null;

    public Client() {
        super("SudoClient");
        addWindowListener(this);
        fullscreen = false;
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initComponents();
        setSize(800, 650);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public WidgetManager getWidgetManager() {
        return widgetManager;
    }

    public void initComponents() {
        widgetManager = new WidgetManager(this);
        add(widgetManager.getMenuBar(), BorderLayout.NORTH);
        add(widgetManager, BorderLayout.CENTER);
        pack();
    }

    public void setFullscreen(final boolean fullscreen) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (Client.this.fullscreen != fullscreen && device.isFullScreenSupported()) {
                    Client.this.fullscreen = fullscreen;

                    if (!fullscreen) {
                        device.setDisplayMode(dispModeOld);
                        setVisible(false);
                        dispose();
                        setUndecorated(false);
                        device.setFullScreenWindow(null);
                        pack();
                        //setSize(800, 600);
                        setLocationRelativeTo(null);
                        setVisible(true);
                    } else {
                        dispModeOld = device.getDisplayMode();
                        setVisible(false);
                        dispose();
                        setUndecorated(true);
                        device.setFullScreenWindow(Client.this);
                    }

                    //pack();
                    widgetManager.getCurrent().gainFocus();
                    repaint();
                }
            }
        });
    }

    public void toggleFullScreen() {
        setFullscreen(!fullscreen);
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        dispose();
        if (widgetManager != null) {
            widgetManager.close();
        }
        System.exit(0);
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
    }
}
