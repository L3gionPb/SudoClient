package com.sudoclient.widgets.preloaded.widgetloader;

import com.sudoclient.widgets.WidgetManager;
import com.sudoclient.widgets.api.WidgetAdapter;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 6:08 AM
 */

@WidgetPreamble(name = "Widgets", author = "Deprecated")
public class WidgetAdapterLoader extends WidgetAdapter {
    private static HashMap<WidgetPreamble, Class<? extends WidgetAdapter>> widgetHashMap;
    private JPanel viewField;
    private WidgetManager ctx;

    public WidgetAdapterLoader(WidgetManager ctx) {
        this.ctx = ctx;
        viewField = new JPanel(new BorderLayout());
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
        widgetHashMap = new HashMap<WidgetPreamble, Class<? extends WidgetAdapter>>();
        //TODO Add more Widgets
    }

    private void fillViewField() {
        if (widgetHashMap.size() == 0) {
            viewField.add(new JLabel("No Widgets have been added yet"), BorderLayout.CENTER);
        } else {
            for (WidgetPreamble widgetPreamble : widgetHashMap.keySet()) {
                viewField.add(new WidgetLoaderComponent(this, widgetPreamble));
            }
        }
    }
}
