package com.sudoclient.widgets.preloaded.worldmap;

import com.sudoclient.widgets.api.WidgetAdapter;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.imageio.ImageIO;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * User: deprecated
 * Date: 5/4/12
 * Time: 6:54 PM
 */

@WidgetPreamble(name = "World Map", author = "Deprecated")
public class WorldMap extends WidgetAdapter implements MouseWheelListener, MouseInputListener {
    private static final BufferedImage MAP;
    private Point lastDragLoc = null;

    static {
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(WorldMap.class.getResource("/resources/images/worldMap.png"));
        } catch (IOException ignored) {
        }
        MAP = temp;
    }

    private ImageViewport imageViewport;

    /**
     * Create a new Widget with a standard BorderLayout
     */
    public WorldMap() {
        imageViewport = new ImageViewport(this, MAP);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageViewport.getViewportClip(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        imageViewport.zoom(event.getWheelRotation());

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            imageViewport.hop(mouseEvent.getPoint());
        }

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        lastDragLoc = mouseEvent.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lastDragLoc = null;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (lastDragLoc != null) {
            imageViewport.translate(lastDragLoc.x - mouseEvent.getX(), lastDragLoc.y - mouseEvent.getY());
            lastDragLoc = mouseEvent.getPoint();
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
