package com.sudoclient.widgets.preloaded;

import com.sudoclient.widgets.api.Manifest;
import com.sudoclient.widgets.api.Widget;

import java.awt.*;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:37 AM
 */

@Manifest(name = "Test Widget", authors = {"Deprecated"})
public class TestWidget extends Widget {
    public TestWidget() {
        add(new Label("Testing...."));
        setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(768, 560));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawString("Testing", 20, 20);
    }
}
