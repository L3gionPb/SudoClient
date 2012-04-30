package com.sudoclient.widgets.api;

import com.sudoclient.widgets.Tab;
import com.sudoclient.widgets.WidgetManager;
import com.sudoclient.widgets.preloaded.runescape.Runescape;

import javax.swing.*;
import java.awt.*;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:32 AM
 */

public class WidgetAdapter extends JPanel {
    private final int idNum;
    private static WidgetManager ctx = null;
    private Tab tab;
    private WidgetPreamble preamble;

    /**
     * Create a new Widget with a standard BorderLayout
     */
    public WidgetAdapter() {
        this(new BorderLayout());
    }

    /**
     * Create a new Widget with a given LayoutManager
     *
     * @param layoutManager the layout manager
     */
    public WidgetAdapter(LayoutManager layoutManager) {
        super(layoutManager);
        if (!(this instanceof Runescape)) {
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        preamble = this.getClass().getAnnotation(WidgetPreamble.class);
        idNum = IdGenerator.getNext();
        tab = new Tab(this);
        gainFocus();
    }

    /**
     * Called when the Widget loses focus
     */
    public void loseFocus() {
    }

    /**
     * Called when the Widget gains focus
     */
    public void gainFocus() {
        requestFocus();
    }

    /**
     * Called when client is shutting down
     */
    public void onShutdown() {
    }

    /**
     * Gets the tab for this widget (Internal use only)
     *
     * @return the Tab
     */
    public final Tab getTab() {
        return tab;
    }

    /**
     * Gets the preamble for this widget (Internal use only)
     *
     * @return the preamble
     */
    public final WidgetPreamble getPreamble() {
        return preamble;
    }

    /**
     * Sets the context for this widget (Internal use only)
     *
     * @param ctx the context
     */
    public static void setContext(WidgetManager ctx) {
        if (WidgetAdapter.ctx != null) {
            throw new RuntimeException("Widget context cannot be reset");
        }

        WidgetAdapter.ctx = ctx;
    }

    /**
     * Compares two objects for equality
     *
     * @param o the object to compare to
     * @return true if o is a Widget and {@link WidgetAdapter#equals(WidgetAdapter)} is true
     */
    @Override
    public final boolean equals(Object o) {
        return (o instanceof WidgetAdapter) && equals((WidgetAdapter) o);
    }

    /**
     * Equality based on assigned ID number on creation
     *
     * @param w widget to compare to
     * @return true if the widgets are equal, else false
     */
    public final boolean equals(WidgetAdapter w) {
        return (w == null ? -1 : w.idNum) == idNum;
    }

    /**
     * Generates ID number for tab (Internal use only)
     */
    private final static class IdGenerator {
        private static int prev = 0;

        private static int getNext() {
            return prev++;
        }
    }
}
