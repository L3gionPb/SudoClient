package com.sudoclient.widgets.preloaded.runescape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 7:39 PM
 */
public class Overlay extends Window {
    private BufferedImage image, buffer;

    public Overlay(JFrame owner, Rectangle bounds) {
        super(owner);
        setVisible(true);
        setAlwaysOnTop(true);
        setBounds(bounds);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawImage(image, 0, 0, null);
    }

    public Graphics getBufferGraphics() {
        image.getGraphics().drawImage(buffer, 0, 0, null);
        return buffer.getGraphics();
    }

    @Override
    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        buffer = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
    }
}
