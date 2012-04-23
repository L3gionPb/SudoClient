package com.sudoclient.widgets;

import com.sudoclient.widgets.api.Manifest;
import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.preloaded.WidgetLoader;
import com.sudoclient.widgets.preloaded.runescape.Runescape;

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
    private Widget widget;
    private Widgets ctx;

    public Tab(Widgets ctx) {
        this.ctx = ctx;
        add(new JLabel(new ImageIcon(this.getClass().getResource("/resources/newTab.png"))));
        setMaximumSize(new Dimension(20, 20));
        widget = null;
        enableInputMethods(true);
        addMouseListener(this);
    }

    public Tab(Widgets ctx, Widget widget) {
        this.ctx = ctx;
        this.widget = widget;
        setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
        add(new JLabel(widget.getClass().getAnnotation(Manifest.class).name()));
        setToolTipText(getAuthorsText() + "\n" + getDescText());
        enableInputMethods(true);
        addMouseListener(this);
    }

    private String getAuthorsText() {
        String s = "Created by ";

        for (String s2 : widget.getClass().getAnnotation(Manifest.class).authors()) {
            s += s2 + "   ";
        }

        return s;
    }

    private String getDescText() {
        return widget.getClass().getAnnotation(Manifest.class).description();
    }

    public Widgets getCtx() {
        return ctx;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (widget != null) {
            ctx.setCurrent(widget);

            if (widget instanceof Runescape) {
                ((Runescape) widget).getClient().requestFocusInWindow();
            } else {
                widget.requestFocusInWindow();
            }
        } else {
            ctx.addWidget(new WidgetLoader());
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
