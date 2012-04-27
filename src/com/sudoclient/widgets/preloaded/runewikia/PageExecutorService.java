package com.sudoclient.widgets.preloaded.runewikia;

import javax.swing.text.JTextComponent;
import java.util.Stack;

/**
 * User: deprecated
 * Date: 4/27/12
 * Time: 2:06 PM
 */
public class PageExecutorService {
    private JTextComponent out;
    private PageLoader currentLoader;
    private Stack<String> pageBacktrack;

    public PageExecutorService(JTextComponent out) {
        this.out = out;
        pageBacktrack = new Stack<String>();
        currentLoader = null;
    }

    public void submit(String url) {
        if (currentLoader != null) {
            currentLoader.kill();
        }

        new Thread((currentLoader = new PageLoader(url, (pageBacktrack.size() != 0 ? pageBacktrack.peek() : null), out) {
            @Override
            public void onFinish() {
                pageBacktrack.push(getFinalizedURL());
            }
        })).start();
    }

    public void back() {
        pageBacktrack.pop();
        submit(pageBacktrack.pop());
    }

    public void follow(String tocLabel) {
        try {
            out.setCaretPosition(out.getText().indexOf("<span class=\"mw-headline\" id=\"" + tocLabel + "\">"));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        currentLoader.kill();
    }
}
