package com.sudoclient.client;

import com.sudoclient.widgets.WidgetManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

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
        try {
            setIconImage(ImageIO.read(getClass().getResource("/resources/icon.png")));
        } catch (IOException ignored) {
        }
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

    public void setFullScreen(final boolean fullScreen) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (Client.this.fullscreen != fullScreen && device.isFullScreenSupported()) {
                    Client.this.fullscreen = fullScreen;

                    if (!fullScreen) {
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
        setFullScreen(!fullscreen);
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        setVisible(false);
        if (widgetManager != null) {
            widgetManager.close();
        }
        dispose();
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
