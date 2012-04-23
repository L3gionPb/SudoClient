package com.sudoclient.widgets.preloaded.widgetloader;

import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.api.WidgetPreamble;
import com.sudoclient.widgets.preloaded.clock.Clock;

import javax.swing.*;
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
    private JTextField search;

    public WidgetLoader() {
        //viewField = new JPanel(new GridLayout(5, 5, 5, 5));
        //fillViewField("", 0);
    }

    private static void loadLocalWidgets() {
        widgetHashMap.put(Clock.class.getAnnotation(WidgetPreamble.class), Clock.class);
    }

    private void fillViewField(String text, int page) {
        for (WidgetPreamble widgetPreamble : widgetHashMap.keySet()) {
            viewField.add(new WidgetLoaderComponent(widgetPreamble));
        }
    }
}
