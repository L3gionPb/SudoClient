package com.sudoclient.widgets;

import com.sudoclient.client.Client;
import com.sudoclient.client.ClientMenu;
import com.sudoclient.client.overlayevents.OverlayListener;
import com.sudoclient.client.overlayevents.OverlayManager;
import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.preloaded.runescape.Runescape;
import com.sudoclient.widgets.preloaded.widgetloader.WidgetLoader;

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
    private final OverlayManager overlayManager;
    private JPanel tabPanel;
    private ClientMenu menu;
    private JPanel menuBar;
    private ArrayList<Widget> widgets;
    private Widget current;
    private Runescape runescape;

    public WidgetManager(Client ctx) {
        super(new BorderLayout());
        this.ctx = ctx;

        menu = new ClientMenu(ctx);
        overlayManager = new OverlayManager(this);

        Widget.setContext(this);
        WidgetLoader.loadLocalWidgets();

        Tab.setContext(this);
        ADD_TAB = new Tab();
        ADD_TAB.setEnabled(false);

        menuBar = new JPanel(new BorderLayout());
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        tabPanel = new JPanel(flowLayout);
        tabPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        widgets = new ArrayList<Widget>();
        current = initRSWidget();
        packTabPanel();
    }

    public void close() {
        overlayManager.kill();
        runescape.kill();
    }

    private Widget initRSWidget() {
        runescape = new Runescape();
        add(runescape, BorderLayout.CENTER);
        widgets.add(runescape);
        return runescape;
    }

    public void addWidget(Widget widget) {
        if (widget instanceof OverlayListener) {
            overlayManager.addListener((OverlayListener) widget);
        }

        widgets.add(widget);
        setCurrent(widget);
    }

    public void removeWidget(Widget widget) {
        if (widget instanceof OverlayListener) {
            overlayManager.removeListener((OverlayListener) widget);
        }

        int index = widgets.indexOf(widget);
        if (widgets.size() > index + 1) {
            setCurrent(widgets.get(index + 1));
        } else {
            setCurrent(widgets.get(index - 1));
        }

        widgets.remove(widget);
        packTabPanel();
        updateUI();
    }

    public void setCurrent(final Widget widget) {
        if (!widget.equals(current)) {
            current.loseFocus();
            if (!(current.equals(runescape))) {
                remove(current);
            }

            if (!(widget.equals(runescape))) {
                add(widget, BorderLayout.CENTER);
            } else {
                ((BorderLayout) getLayout()).addLayoutComponent(runescape, BorderLayout.CENTER);
            }
            widget.gainFocus();

            current = widget;
            packTabPanel();
        }
    }

    public Widget getCurrent() {
        return current;
    }

    public void packTabPanel() {
        tabPanel.removeAll();
        for (Widget widget : widgets) {
            Tab tab = widget.getTab();
            tab.setSelected(widget.equals(current));
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
