package com.sudoclient.widgets.api;

import com.sudoclient.widgets.Tab;

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
    private WidgetPreamble preamble;

    /**
     * Create a new Widget with a standard BorderLayout
     */
    public Widget() {
        this(new BorderLayout());
    }

    /**
     * Create a new Widget with a given LayoutManager
     *
     * @param layoutManager the layout manager
     */
    public Widget(LayoutManager layoutManager) {
        super(layoutManager);
        preamble = this.getClass().getAnnotation(WidgetPreamble.class);
        idNum = IdGenerator.getNext();
        tab = new Tab(this);
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
     * Compares two objects for equality
     *
     * @param o the object to compare to
     * @return true if o is a Widget and {@link Widget#equals(Widget)} is true
     */
    @Override
    public final boolean equals(Object o) {
        return (o instanceof Widget) && equals((Widget) o);
    }

    /**
     * Equality based on assigned ID number on creation
     *
     * @param w widget to compare to
     * @return true if the widgets are equal, else false
     */
    public final boolean equals(Widget w) {
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
