package com.sudoclient.widgets;

import com.sudoclient.client.Client;
import com.sudoclient.client.ClientMenu;
import com.sudoclient.widgets.api.WidgetAdapter;
import com.sudoclient.widgets.preloaded.runescape.Runescape;
import com.sudoclient.widgets.preloaded.widgetloader.WidgetAdapterLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:56 AM
 */

public class WidgetManager extends JPanel {
    private final Client ctx;
    private final Tab ADD_TAB;
    private JPanel tabPanel;
    private ClientMenu menu;
    private JPanel menuBar;
    private ArrayList<WidgetAdapter> widgetAdapters;
    private WidgetAdapter current;
    private Runescape runescape;

    public WidgetManager(Client ctx) {
        super(new BorderLayout());
        this.ctx = ctx;

        menu = new ClientMenu(ctx);
        WidgetAdapter.setContext(this);
        WidgetAdapterLoader.loadLocalWidgets();

        Tab.setContext(this);
        ADD_TAB = new Tab();
        ADD_TAB.setEnabled(false);

        menuBar = new JPanel(new BorderLayout());
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        tabPanel = new JPanel(flowLayout);
        tabPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        widgetAdapters = new ArrayList<WidgetAdapter>();
        current = initRSWidget();
        packTabPanel();
    }

    public void close() {
        for (WidgetAdapter widgetAdapter : widgetAdapters) {
            widgetAdapter.onShutdown();
        }
    }

    private WidgetAdapter initRSWidget() {
        runescape = new Runescape();
        add(runescape, BorderLayout.CENTER);
        widgetAdapters.add(runescape);
        return runescape;
    }

    public void addWidget(WidgetAdapter widgetAdapter) {
        widgetAdapters.add(widgetAdapter);
        setCurrent(widgetAdapter);
    }

    public void removeWidget(WidgetAdapter widgetAdapter) {
        int index = widgetAdapters.indexOf(widgetAdapter);
        if (widgetAdapters.size() > index + 1) {
            setCurrent(widgetAdapters.get(index + 1));
        } else {
            setCurrent(widgetAdapters.get(index - 1));
        }

        widgetAdapters.remove(widgetAdapter);
        packTabPanel();
        updateUI();
    }

    public void setCurrent(final WidgetAdapter widgetAdapter) {
        if (!widgetAdapter.equals(current)) {
            current.loseFocus();
            if (!(current.equals(runescape))) {
                remove(current);
            }

            if (!(widgetAdapter.equals(runescape))) {
                add(widgetAdapter, BorderLayout.CENTER);
            } else {
                ((BorderLayout) getLayout()).addLayoutComponent(runescape, BorderLayout.CENTER);
            }
            widgetAdapter.gainFocus();

            current = widgetAdapter;
            packTabPanel();
        }
    }

    public WidgetAdapter getCurrent() {
        return current;
    }

    public Runescape getRunescape() {
        return runescape;
    }

    public void packTabPanel() {
        tabPanel.removeAll();
        for (WidgetAdapter widgetAdapter : widgetAdapters) {
            Tab tab = widgetAdapter.getTab();
            tab.setSelected(widgetAdapter.equals(current));
            tabPanel.add(tab);
        }
        tabPanel.add(ADD_TAB);
        tabPanel.updateUI();
    }

    public JPanel getMenuBar() {
        menuBar.removeAll();
        menuBar.add(tabPanel, BorderLayout.WEST);
        menuBar.add(menu, BorderLayout.EAST);
        menuBar.updateUI();

        return menuBar;
    }
}
