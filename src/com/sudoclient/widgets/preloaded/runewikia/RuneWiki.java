package com.sudoclient.widgets.preloaded.runewikia;

import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * User: deprecated
 * Date: 4/24/12
 * Time: 7:20 AM
 */

@WidgetPreamble(name = "RuneWikia", authors = {"Deprecated"})
public class RuneWiki extends Widget implements HyperlinkListener {
    private static final URL CSS = RuneWiki.class.getResource("/resources/css/runewikia.css");
    private final String[] SEARCH_URL = {"http://runescape.wikia.com/wiki/index.php?search=", "&fulltext=0"};
    private PageExecutorService executor;

    public RuneWiki(String search) {
        JEditorPane page = new JEditorPane();
        page.setContentType("text/html");
        page.setEditable(false);
        page.addHyperlinkListener(this);
        HTMLEditorKit ek = new HTMLEditorKit();
        page.setEditorKit(ek);
        setStyle(ek.getStyleSheet());

        JScrollPane jsp = new JScrollPane(page, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jsp, BorderLayout.CENTER);

        executor = new PageExecutorService(page);
        searchWiki(search);
    }

    public void searchWiki(String search) {
        search = search.replaceAll(" ", "+");
        executor.submit(SEARCH_URL[0] + search + SEARCH_URL[1]);
    }

    /**
     * Called when the Widget gains focus
     */
    @Override
    public void gainFocus() {
        revalidate();
    }

    /**
     * Called when client is shutting down
     */
    @Override
    public void onShutdown() {
        executor.shutdown();
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (event.getDescription().startsWith("#")) {
                //executor.follow(event.getDescription().substring(1));
                //TODO build a hashmap of caret positions for TOC ahrefs
            } else if (event.getDescription().equals("BACK")) {
                executor.back();
            } else {
                executor.submit(event.getDescription());
            }
        }
    }

    private void setStyle(StyleSheet ss) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(CSS.openStream()));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ss.addRule(sb.toString());
    }
}