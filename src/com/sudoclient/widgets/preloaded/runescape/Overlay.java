package com.sudoclient.widgets.preloaded.runescape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 7:39 PM
 */
public class Overlay extends JPanel {
    private BufferedImage image, buffer;

    public Overlay() {
        super();
        setOpaque(false);
        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        buffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        //graphics.drawImage(image, 0, 0, null);
        graphics.setColor(Color.RED);
        graphics.drawString("TESTING", 100, 100);
    }

    public Graphics getBufferGraphics() {
        image.getGraphics().drawImage(buffer, 0, 0, null);
        return buffer.getGraphics();
    }
}
