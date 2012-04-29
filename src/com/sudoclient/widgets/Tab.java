package com.sudoclient.widgets;

import com.sudoclient.widgets.api.WidgetAdapter;
import com.sudoclient.widgets.preloaded.runescape.Runescape;
import com.sudoclient.widgets.preloaded.widgetloader.WidgetAdapterLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:57 AM
 */
public class Tab extends JToggleButton implements MouseListener {
    private static WidgetManager ctx = null;
    private WidgetAdapter widgetAdapter;

    /**
     * Create a new tab button
     */
    public Tab() {
        this(null);
    }

    /**
     * Create a tab for a defined widget
     *
     * @param widgetAdapter the widget
     */
    public Tab(WidgetAdapter widgetAdapter) {
        this.widgetAdapter = widgetAdapter;

        if (widgetAdapter == null || widgetAdapter.getPreamble() == null) {
            setMaximumSize(new Dimension(20, 20));
            setIcon(new ImageIcon(this.getClass().getResource("/resources/images/newTab.png")));
            setToolTipText("Open up a new Tab");
        } else {
            setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
            add(new JLabel(widgetAdapter.getPreamble().name()));
            setToolTipText(getAuthorsText() + "\n" + getDescText());
        }

        enableInputMethods(true);
        addMouseListener(this);
        setEnabled(false);
    }

    /**
     * Set the context of the Tabs (Internal use only)
     *
     * @param ctx the context
     * @throws RuntimeException if context has been set
     */
    public static void setContext(WidgetManager ctx) throws RuntimeException {
        if (Tab.ctx == null) {
            Tab.ctx = ctx;
        } else {
            throw new RuntimeException("Context has already been set, cannot be changed");
        }
    }

    private String getAuthorsText() {
        String s = "Created by ";

        for (String s2 : widgetAdapter.getPreamble().authors()) {
            s += s2 + "   ";
        }

        return s;
    }

    private String getDescText() {
        return widgetAdapter.getPreamble().description();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (widgetAdapter != null) {
            if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                ctx.setCurrent(widgetAdapter);
            } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                if (widgetAdapter instanceof Runescape) {
                    ((Runescape) widgetAdapter).reset();


                } else {
                    ctx.removeWidget(widgetAdapter);
                }
            }
        } else {
            if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                ctx.addWidget(new WidgetAdapterLoader(ctx));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
