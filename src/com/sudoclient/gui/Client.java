package com.sudoclient.gui;

import com.sudoclient.widgets.Widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * User: deprecated
 * Date: 4/21/12
 * Time: 5:56 PM
 */

public class Client extends JFrame implements ComponentListener {
    private Widgets widgets;

    public Client() {
        super("SudoClient");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setSize(768, 600);
        addComponentListener(this);
        setVisible(true);
    }

    public void initComponents() {
        widgets = new Widgets();
        add(widgets.getTabPanel(), BorderLayout.NORTH);
        add(widgets, BorderLayout.CENTER);
        pack();
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        widgets.updateUI();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }
}
