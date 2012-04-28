package com.sudoclient.client;

import com.sudoclient.widgets.preloaded.imgur.ScreenShotFactory;
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
    private final Client ctx;
    private final JTextField search;
    private final JToggleButton fullScreenButton, screenShotButton;

    public ClientMenu(Client ctx) {
        super(new FlowLayout(FlowLayout.RIGHT));
        this.ctx = ctx;

        search = new JTextField("Search RuneWikia...");
        search.setColumns(20);
        search.addMouseListener(this);
        search.addKeyListener(this);

        ImageIcon expandIcon = new ImageIcon(this.getClass().getResource("/resources/expand.png"));
        fullScreenButton = new JToggleButton(expandIcon);
        fullScreenButton.enableInputMethods(true);
        fullScreenButton.setEnabled(false);
        //fullScreenButton.addMouseListener(this);

        ImageIcon screenShotIcon = new ImageIcon(this.getClass().getResource("/resources/screenshot.png"));
        screenShotButton = new JToggleButton(screenShotIcon);
        screenShotButton.enableInputMethods(true);
        screenShotButton.setEnabled(false);
        screenShotButton.addMouseListener(this);

        add(search);
        add(screenShotButton);
        add(fullScreenButton);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getComponent().equals(fullScreenButton)) {
            ctx.toggleFullScreen();
        } else if (mouseEvent.getComponent().equals(search)) {
            search.selectAll();
        } else if (mouseEvent.getComponent().equals(screenShotButton)) {
            Rectangle temp = ctx.getWidgetManager().getBounds();
            temp.translate(ctx.getRootPane().getX(), ctx.getRootPane().getY());
            temp.translate(ctx.getX(), ctx.getY());
            ScreenShotFactory.takeScreenShot(temp);
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
