package com.sudoclient.widgets;

import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.preloaded.runescape.Runescape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:56 AM
 */
public class Widgets extends JPanel {
    private JPanel tabPanel;
    private ArrayList<Widget> widgets;
    private Widget current;
    private Runescape runescape;

    public Widgets() {
        super(new BorderLayout());

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        tabPanel = new JPanel(flowLayout);
        tabPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 22));

        widgets = new ArrayList<Widget>();
        current = initRSWidget();
        packTabPanel();
    }

    private Widget initRSWidget() {
        runescape = new Runescape();
        runescape.setTab(new Tab(this, runescape));
        add(runescape, BorderLayout.CENTER);
        widgets.add(runescape);
        return runescape;
    }

    public void addWidget(Widget widget) {
        widget.setTab(new Tab(this, widget));
        widgets.add(widget);
        setCurrent(widget);
    }

    public void removeWidget(Widget widget) {
        widgets.remove(widget);
        packTabPanel();
    }

    public void setCurrent(Widget widget) {
        if (current != widget) {
            if (current instanceof Runescape) {
                current.setVisible(false);
            } else {
                remove(current);
            }

            current = widget;

            if (current instanceof Runescape) {
                current.setVisible(true);

            } else {
                add(current, BorderLayout.CENTER);
            }

            current.setSize(getSize());
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
            if (widget.equals(current)) {
                tab.setSelected(true);
                tab.setEnabled(false);
            } else {
                tab.setSelected(false);
            }

            tabPanel.add(tab);
        }
        tabPanel.add(new Tab(this));
        updateUI();
    }

    public JPanel getTabPanel() {
        return tabPanel;
    }

    @Override
    public void setSize(int i, int i1) {
        super.setSize(i, i1);
        if (runescape != null) {
            runescape.setSize(i, i1);
            current.setSize(i, i1);
        }
    }
}
