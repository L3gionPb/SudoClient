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
    private final JFrame ctx;
    private final Tab ADD_TAB;
    private JPanel tabPanel;
    private ArrayList<Widget> widgets;
    private Widget current;
    private Runescape runescape;

    public Widgets(JFrame ctx) {
        super(new BorderLayout());
        this.ctx = ctx;

        Tab.setContext(this);
        ADD_TAB = new Tab();
        ADD_TAB.setEnabled(false);

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
        add(runescape, BorderLayout.CENTER);
        widgets.add(runescape);
        return runescape;
    }

    public void addWidget(Widget widget) {
        widgets.add(widget);
        setCurrent(widget);
    }

    public void removeWidget(Widget widget) {
        widgets.remove(widget);
        packTabPanel();
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

    public JPanel getTabPanel() {
        return tabPanel;
    }
}
