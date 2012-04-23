package com.sudoclient.widgets.api;

import com.sudoclient.widgets.Tab;
import com.sudoclient.widgets.Widgets;

import javax.swing.*;
import java.awt.*;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:32 AM
 */

public class Widget extends JPanel {
    private Tab tab;
    private int idNum;

    public Widget() {
        super(new BorderLayout());
        idNum = IdGenerator.getNext();
    }

    public final void setTab(Tab tab) {
        this.tab = tab;
    }

    public final Tab getTab() {
        return tab;
    }

    public final Widgets getContext() {
        return tab.getCtx();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Widget) && ((Widget) o).idNum == idNum;
    }

    private static class IdGenerator {
        private static int prev = 0;

        private static int getNext() {
            return prev++;
        }
    }
}
