package com.sudoclient.widgets.preloaded;

import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.*;
import java.util.HashMap;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 6:08 AM
 */

@WidgetPreamble(name = "Widgets", authors = {"Deprecated"})
public class WidgetLoader extends Widget {
    private HashMap<WidgetPreamble, Widget> widgetHashMap;
    private JPanel viewField;
    private JTextField search;

    public WidgetLoader() {

    }

    private void fillViewField(String text, int page) {

    }
}
