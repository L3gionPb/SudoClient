package com.sudoclient.widgets.preloaded.clock;

import com.sudoclient.client.overlayevents.OverlayListener;
import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.api.WidgetPreamble;

import java.awt.*;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 7:17 AM
 */

@WidgetPreamble(name = "Clock", authors = {"Deprecated"})
public class Clock extends Widget implements OverlayListener {
    private long startTime;

    public Clock() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void paintOverlay(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Time: " + (System.currentTimeMillis() - startTime), 20, 20);
    }
}
