package com.sudoclient.gui;

import com.sudoclient.widgets.Widgets;

import javax.swing.*;
import java.awt.*;

/**
 * User: deprecated
 * Date: 4/21/12
 * Time: 5:56 PM
 */

public class Client extends JFrame {
    private Widgets widgets;

    public Client() {
        super("SudoClient");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setSize(800, 650);
        setVisible(true);
    }

    public void initComponents() {
        widgets = new Widgets(this);
        add(widgets.getTabPanel(), BorderLayout.NORTH);
        add(widgets, BorderLayout.CENTER);
        pack();
    }
}
