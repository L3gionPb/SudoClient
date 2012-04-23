package com.sudoclient.widgets.preloaded.runescape;

import com.sudoclient.widgets.api.Widget;
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

@WidgetPreamble(name = "Runescape", authors = {"Jagex"})
public final class Runescape extends Widget implements Runnable, AppletStub {
    private Applet client = new Applet();
    private RSClassLoader loader;
    private JLabel splash;

    public Runescape() {
        setBackground(Color.BLACK);
        splash = new JLabel(new ImageIcon(this.getClass().getResource("/resources/splash.gif")));
        add(splash, BorderLayout.CENTER);
        new Thread(this).start();
    }

    /**
     * The run void of the loader
     */
    public void run() {
        try {
            loader = new RSClassLoader();
            Class<?> c = loader.loadClass("Rs2Applet");
            client = (Applet) c.newInstance();
            client.setStub(this);
            add(client, BorderLayout.CENTER);
            client.init();
            client.start();
            remove(splash);
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        requestFocus();
    }

    public Applet getClient() {
        return client;
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
