package com.sudoclient.widgets.preloaded.widgetloader;

import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 7:22 AM
 */
public class WidgetLoaderComponent extends JPanel implements MouseListener {
    private WidgetPreamble preamble;
    private WidgetLoader ctx;

    public WidgetLoaderComponent(WidgetLoader ctx, WidgetPreamble preamble) {
        addMouseListener(this);
        this.ctx = ctx;
        this.preamble = preamble;
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(preamble.name(), 10, 5);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        ctx.consumeWidget(preamble);
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
