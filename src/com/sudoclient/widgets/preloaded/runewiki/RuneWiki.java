package com.sudoclient.widgets.preloaded.runewiki;

import com.sudoclient.widgets.api.Widget;
import com.sudoclient.widgets.api.WidgetPreamble;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: deprecated
 * Date: 4/24/12
 * Time: 7:20 AM
 */

@WidgetPreamble(name = "RuneWikia", authors = {"Deprecated"})
public class RuneWiki extends Widget implements HyperlinkListener {
    private final String[] SEARCH_URL = {"http://runescape.wikia.com/wiki/index.php?search=", "&fulltext=0"};
    private URL root;
    private JEditorPane page;

    public RuneWiki(String search) {
        search = search.replaceAll(" ", "_");
        try {
            root = new URL("http://runescape.wikia.com");
        } catch (MalformedURLException ignored) {
        }

        page = new JEditorPane();
        page.setContentType("text/html");
        page.setEditable(false);
        page.addHyperlinkListener(this);

        try {
            page.setText(pageParser(new URL(SEARCH_URL[0] + search + SEARCH_URL[1])));
            page.setCaretPosition(0);
        } catch (Exception e) {
            page.setText("Error: " + e);
        }

        JScrollPane jsp = new JScrollPane(page, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jsp, BorderLayout.CENTER);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                page.setText(pageParser(new URL(root, event.getURL().getPath())));
                page.setCaretPosition(0);
            } catch (IOException e) {
                page.setText("Error: " + e);
            }
        }
    }


    private String pageParser(URL url) throws IOException {
        StringBuilder pageText = new StringBuilder();
        boolean writingBody = false, finished = false;
        int divCount = 0;
        pageText.append("<html>\n<head>\n<title> RuneWikia </title>\n</head>\n<body>\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;

        while (!finished && (line = reader.readLine()) != null) {
            if (writingBody || line.contains("<div id=\"WikiaArticle\" class=\"WikiaArticle\">")) {
                if (!writingBody) {
                    writingBody = true;
                }
                line = line.replaceAll("<a href=\"/wiki/", "<a href=\"http://runescape.wikia.com/wiki/");
                line = line.replaceAll("<span class=\"editsection\">.*</span>", "");
                line = line.replaceAll("<figcaption.*</figcaption>", "");
                line = line.replaceAll("<img", "<img border=\"0\"");

                pageText.append(line);
                pageText.append("\n");

                while (line.contains("<div")) {
                    divCount++;
                    line = line.replaceFirst("<div", "----");
                }

                while (line.contains("</div>")) {
                    divCount--;
                    line = line.replaceFirst("</div>", "-----");

                    if (divCount == 0) {
                        System.out.println("Body filled");
                        finished = true;
                    }
                }
            }
        }

        reader.close();
        pageText.append("\n</body>\n</html>");

        String out = pageText.toString();
        out = out.replaceAll("<!--[\\s\\S]+-->", "");
        out = out.replaceAll("<script>.+</script>", "");

        System.out.println(out);
        return out;
    }
}
