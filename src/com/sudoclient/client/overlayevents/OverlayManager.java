package com.sudoclient.client.overlayevents;

import com.sudoclient.widgets.Widgets;

import java.awt.*;
import java.util.ArrayList;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 10:27 AM
 */

public class OverlayManager implements Runnable {
    private static OverlayManager instance = null;
    private final Widgets ctx;
    private final Object lock = new Object();
    private boolean running;
    private ArrayList<OverlayListener> listeners;
    private ArrayList<OverlayDispatcher> dispatchers;

    public OverlayManager(Widgets ctx) {
        if (instance != null) {
            throw new RuntimeException("Cannot create multiple Overlay Managers");
        }

        this.ctx = ctx;
        listeners = new ArrayList<OverlayListener>();
        dispatchers = new ArrayList<OverlayDispatcher>();
        running = true;

        new Thread(this).start();
    }

    public void addListener(OverlayListener listener) {
        synchronized (lock) {
            listeners.add(listener);
        }
    }

    public void addDispatcher(OverlayDispatcher dispatcher) {
        synchronized (lock) {
            dispatchers.add(dispatcher);
        }
    }

    public void removeListener(OverlayListener listener) {
        synchronized (lock) {
            listeners.remove(listener);
        }
    }

    public void removeDispatcher(OverlayDispatcher dispatcher) {
        synchronized (lock) {
            dispatchers.remove(dispatcher);
        }
    }

    public void kill() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (lock) {
                if (ctx.getCurrent() instanceof OverlayDispatcher) {
                    for (OverlayDispatcher dispatcher : dispatchers) {
                        Graphics g = dispatcher.getOverlayGraphics();

                        if (g != null) {
                            for (OverlayListener listener : listeners) {
                                listener.paintOverlay(g);
                            }

                            g.dispose();
                        }
                    }
                }
            }

            try {
                Thread.sleep(70);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
