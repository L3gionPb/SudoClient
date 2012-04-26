package com.sudoclient.client;

import com.sudoclient.widgets.preloaded.runewikia.RuneWiki;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * User: deprecated
 * Date: 4/23/12
 * Time: 10:26 PM
 */
public class ClientMenu extends JPanel implements MouseListener, KeyListener {
    private final ImageIcon expandIcon;
    private final Client ctx;
    private final JToggleButton fullScreenButton;
    private final JTextField search;

    public ClientMenu(Client ctx) {
        super(new FlowLayout(FlowLayout.RIGHT));
        this.ctx = ctx;

        search = new JTextField("Search RuneWikia...");
        search.setColumns(20);
        search.addMouseListener(this);
        search.addKeyListener(this);

        expandIcon = new ImageIcon(this.getClass().getResource("/resources/expand.png"));
        fullScreenButton = new JToggleButton(expandIcon);
        fullScreenButton.enableInputMethods(true);
        fullScreenButton.setEnabled(false);
        //fullScreenButton.addMouseListener(this);

        add(search);
        add(fullScreenButton);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getComponent() instanceof JToggleButton) {
            ctx.toggleFullScreen();
        } else {
            search.selectAll();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (search.hasFocus() && (keyEvent.isActionKey() || keyEvent.getKeyCode() == KeyEvent.VK_ENTER)) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (ctx.getWidgetManager().getCurrent() instanceof RuneWiki) {
                        ((RuneWiki) ctx.getWidgetManager().getCurrent()).searchWiki(search.getText());
                        search.setText("");
                    } else {
                        ctx.getWidgetManager().addWidget(new RuneWiki(search.getText()));
                        search.setText("");
                    }
                }
            });
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
