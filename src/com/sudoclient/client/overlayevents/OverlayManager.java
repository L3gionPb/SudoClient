package com.sudoclient.client.overlayevents;

import com.sudoclient.widgets.WidgetManager;

import java.util.ArrayList;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 10:27 AM
 */

public class OverlayManager implements Runnable {
    private static OverlayManager instance = null;
    private final WidgetManager ctx;
    private final Object lock = new Object();
    private boolean running;
    private ArrayList<OverlayListener> listeners;

    public OverlayManager(WidgetManager ctx) {
        if (instance != null) {
            throw new RuntimeException("Cannot create multiple Overlay Managers");
        }

        this.ctx = ctx;
        listeners = new ArrayList<OverlayListener>();
        running = true;

        //new Thread(this).start();
    }

    public void addListener(OverlayListener listener) {
        synchronized (lock) {
            listeners.add(listener);
        }
    }

    public void removeListener(OverlayListener listener) {
        synchronized (lock) {
            listeners.remove(listener);
        }
    }

    public void kill() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (lock) {
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
