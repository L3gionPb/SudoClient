package com.sudoclient.widgets.preloaded.runescape;

import com.sudoclient.widgets.api.WidgetAdapter;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.net.URL;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 9:17 AM
 */

@WidgetPreamble(name = "RuneScape", author = "Deprecated")
public final class Runescape extends WidgetAdapter implements Runnable, AppletStub {
    private final Object lock = new Object();
    private Applet client = new Applet();
    private RSClassLoader loader;
    private JLabel splash;
    private boolean alive, loaded;

    public Runescape() {
        setBackground(Color.BLACK);
        splash = new JLabel(new ImageIcon(this.getClass().getResource("/resources/images/splash.gif")));
        add(splash, BorderLayout.CENTER);

        alive = true;
        loaded = false;
        new Thread(this).start();
    }

    /**
     * The run void of the loader
     */
    public void run() {
        loaded = false;
        synchronized (lock) {
            if (client != null) {
                try {
                    if (alive) {
                        loader = new RSClassLoader();
                    }

                    if (alive) {
                        Class<?> c = loader.loadClass("Rs2Applet");
                        client = (Applet) c.newInstance();
                        client.setStub(this);
                        add(client, BorderLayout.CENTER);
                    }

                    if (alive) {
                        client.init();
                    }

                    if (alive) {
                        client.start();
                    }

                    remove(splash);
                    updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        loaded = true;
    }

    public void reset() {
        if (loaded) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you wish to restart Runescape?",
                    "Confirm Runescape Restart", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                client.stop();
                client.destroy();
                removeAll();
                add(splash, BorderLayout.CENTER);
                updateUI();
                new Thread(this).start();
            }
        }
    }

    /**
     * Called when client is shutting down
     */
    @Override
    public void onShutdown() {
        alive = false;
        client.stop();
        client.destroy();
        client = null;
    }

    /**
     * Called when the Widget loses focus
     */
    @Override
    public void loseFocus() {
        setVisible(false);
    }

    /**
     * Called when the Widget gains focus
     */
    @Override
    public void gainFocus() {
        setVisible(true);
        if (client != null) {
            client.requestFocus();
        }
    }

    public Applet getClient() {
        return client;
    }

    public RSClassLoader getLoader() {
        return loader;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return loader.getBaseURL();
    }

    @Override
    public URL getCodeBase() {
        return loader.getBaseURL();
    }

    @Override
    public String getParameter(String s) {
        return loader.getParameters().get(s);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(int width, int height) {
    }
}
