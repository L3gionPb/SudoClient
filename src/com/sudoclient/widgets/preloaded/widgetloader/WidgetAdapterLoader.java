package com.sudoclient.widgets.preloaded.widgetloader;

import com.sudoclient.client.components.WidgetManager;
import com.sudoclient.widgets.api.WidgetAdapter;
import com.sudoclient.widgets.api.WidgetPreamble;
import com.sudoclient.widgets.preloaded.worldmap.WorldMap;

import javax.swing.*;
import java.awt.*;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 6:08 AM
 */

@WidgetPreamble(name = "Widgets", author = "Deprecated")
public class WidgetAdapterLoader extends WidgetAdapter {
    private final static String[] headings = {"Widget Name", "Description", "Author", "Version"};
    private final WidgetManager ctx;
    private final WidgetTableModel model;
    private final JTable table;

    public WidgetAdapterLoader(final WidgetManager ctx) {
        this.ctx = ctx;
        model = new WidgetTableModel();
        loadLocalWidgets();

        table = new JTable(model.getValues(), headings);
        table.setCellSelectionEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        final JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        add(new WidgetLoaderComponent(this), BorderLayout.SOUTH);
        updateUI();
    }

    public void consumeWidget() {
        try {
            ctx.removeWidget(this);
            ctx.addWidget(model.getWidget(table.getSelectedRow()).newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLocalWidgets() {
        model.addWidget(WorldMap.class);
    }
}
