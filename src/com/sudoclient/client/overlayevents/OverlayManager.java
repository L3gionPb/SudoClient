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
    private ArrayList<OverlayTarget> targets;
    private ArrayList<OverlayListener> listeners;

    public OverlayManager(Widgets ctx) {
        if (instance != null) {
            throw new RuntimeException("Cannot create multiple Overlay Managers");
        }

        this.ctx = ctx;
        targets = new ArrayList<OverlayTarget>();
        listeners = new ArrayList<OverlayListener>();
        running = true;

        new Thread(this).start();
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

    public void addTarget(OverlayTarget target) {
        synchronized (lock) {
            targets.add(target);
        }
    }

    public void removeTarget(OverlayTarget target) {
        synchronized (lock) {
            targets.remove(target);
        }
    }


    public void kill() {
        running = false;
    }

    @Override
    public void run() {
        Graphics g;
        while (running) {
            synchronized (lock) {
                if (ctx.getCurrent() instanceof OverlayTarget && targets.contains(ctx.getCurrent())) {
                    g = ((OverlayTarget) ctx.getCurrent()).getOverlayGraphics();

                    if (!(g == null)) {
                        for (OverlayListener listener : listeners) {
                            listener.renewOverlay(g);
                        }
                        g.dispose();
                    }
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
