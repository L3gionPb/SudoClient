package com.sudoclient.widgets.preloaded.widgetloader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 7:22 AM
 */

class WidgetLoaderComponent extends JPanel implements ActionListener {
    private WidgetAdapterLoader ctx;

    WidgetLoaderComponent(WidgetAdapterLoader ctx) {
        this.ctx = ctx;

        JButton load = new JButton("Load Widget");
        load.addActionListener(this);
        add(load);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ctx.consumeWidget();
    }
}
