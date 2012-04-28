package com.sudoclient.widgets.preloaded.widgetloader;

import com.sudoclient.widgets.WidgetManager;
import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 6:08 AM
 */

@WidgetPreamble(name = "Widgets", authors = {"Deprecated"})
public class WidgetLoader extends Widget {
    private static HashMap<WidgetPreamble, Class<? extends Widget>> widgetHashMap;
    private JPanel viewField;
    private WidgetManager ctx;

    public WidgetLoader(WidgetManager ctx) {
        this.ctx = ctx;
        viewField = new JPanel(new GridLayout(3, 0, 5, 5));
        fillViewField();
        add(viewField, BorderLayout.CENTER);
    }

    public void consumeWidget(WidgetPreamble preamble) {
        try {
            ctx.removeWidget(this);
            ctx.addWidget(widgetHashMap.get(preamble).newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadLocalWidgets() {
        widgetHashMap = new HashMap<WidgetPreamble, Class<? extends Widget>>();
        //TODO Add more Widgets
    }

    private void fillViewField() {
        for (WidgetPreamble widgetPreamble : widgetHashMap.keySet()) {
            viewField.add(new WidgetLoaderComponent(this, widgetPreamble));
        }
    }
}
