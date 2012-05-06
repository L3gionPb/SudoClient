package com.sudoclient.client.components;

import com.sudoclient.client.Client;
import com.sudoclient.widgets.preloaded.imgur.ScreenShot;
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
    private final ImageIcon screenShotIcon, loadingSSIcon, expandIcon, unexpandIcon;
    private boolean ssBlock;

    public ClientMenu(Client ctx) {
        super(new FlowLayout(FlowLayout.RIGHT));
        this.ctx = ctx;

        search = new JTextField("Search RuneWikia...");
        search.setColumns(20);
        search.addMouseListener(this);
        search.addKeyListener(this);

        expandIcon = new ImageIcon(this.getClass().getResource("/resources/images/expand.png"));
        unexpandIcon = new ImageIcon(this.getClass().getResource("/resources/images/unexpand.png"));
        fullScreenButton = new JToggleButton(expandIcon);
        fullScreenButton.enableInputMethods(true);
        fullScreenButton.setEnabled(false);
        //fullScreenButton.addMouseListener(this);

        screenShotIcon = new ImageIcon(this.getClass().getResource("/resources/images/screenshot.png"));
        loadingSSIcon = new ImageIcon(this.getClass().getResource("/resources/images/loading.gif"));
        screenShotButton = new JToggleButton(screenShotIcon);
        screenShotButton.setToolTipText("Take a ScreenShot");
        screenShotButton.enableInputMethods(true);
        screenShotButton.setEnabled(false);
        screenShotButton.addMouseListener(this);
        ssBlock = false;

        add(search);
        add(screenShotButton);
        //add(fullScreenButton);
    }

    public void screenShotFinished() {
        screenShotButton.setIcon(screenShotIcon);
        ssBlock = false;
    }

    public boolean isFullScreen() {
        return ctx.isFullscreenMode();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getComponent().equals(fullScreenButton)) {
            ctx.toggleFullScreen();
            if (ctx.isFullscreenMode()) {
                fullScreenButton.setIcon(expandIcon);
            } else {
                fullScreenButton.setIcon(unexpandIcon);
            }
        } else if (mouseEvent.getComponent().equals(search)) {
            search.selectAll();
        } else if (!ssBlock && mouseEvent.getComponent().equals(screenShotButton)) {
            ssBlock = true;
            Rectangle temp = ctx.getWidgetManager().getBounds();
            temp.translate(ctx.getRootPane().getX(), ctx.getRootPane().getY());
            temp.translate(ctx.getX(), ctx.getY());

            screenShotButton.setIcon(loadingSSIcon);
            ScreenShot.takeScreenShot(temp, this);
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
        if (search.hasFocus() && (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)) {
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
