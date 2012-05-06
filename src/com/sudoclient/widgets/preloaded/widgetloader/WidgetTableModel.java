package com.sudoclient.widgets.preloaded.widgetloader;

import com.sudoclient.widgets.api.WidgetAdapter;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: deprecated
 * Date: 5/6/12
 * Time: 2:57 AM
 */

class WidgetTableModel extends AbstractTableModel {

    private final LinkedList<Class<? extends WidgetAdapter>> widgets;

    WidgetTableModel() {
        widgets = new LinkedList<Class<? extends WidgetAdapter>>();
    }

    void addWidget(final Class<? extends WidgetAdapter> widget) {
        widgets.add(widget);
    }

    @Override
    public int getRowCount() {
        return widgets.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int row, int col) {
        final WidgetPreamble temp = widgets.get(row).getAnnotation(WidgetPreamble.class);
        switch (col) {
            case 0:
                return temp.name();
            case 1:
                return temp.description();
            case 2:
                return temp.author();
            default:
                return temp.version();
        }
    }

    Class<? extends WidgetAdapter> getWidget(final int index) {
        return widgets.get(index);
    }

    Object[][] getValues() {
        final ArrayList<Object[]> tableList = new ArrayList<Object[]>();

        for (Class<? extends WidgetAdapter> clazz : widgets) {
            tableList.add(getValue(clazz));
        }

        return tableList.toArray(new Object[tableList.size()][4]);
    }

    private Object[] getValue(Class<? extends WidgetAdapter> clazz) {
        final WidgetPreamble preamble = clazz.getAnnotation(WidgetPreamble.class);
        return new Object[]{preamble.name(), preamble.description(), preamble.author(), preamble.version()};
    }
}
