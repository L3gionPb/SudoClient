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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: deprecated
 * Date: 4/24/12
 * Time: 7:20 AM
 */

@WidgetPreamble(name = "RuneWikia", authors = {"Deprecated"})
public class RuneWiki extends Widget implements HyperlinkListener {
    private static final URL css = RuneWiki.class.getResource("/resources/runewikia.css");
    private static final Pattern BACKTRACK = Pattern.compile("Retrieved from \"<a href=\"(.+)\">.+</a>\"");
    private final String[] SEARCH_URL = {"http://runescape.wikia.com/wiki/index.php?search=", "&fulltext=0"};
    private URL root;
    private JEditorPane page;
    private Stack<String> pageBacktrack;

    public RuneWiki(String search) {
        try {
            root = new URL("http://runescape.wikia.com");
        } catch (MalformedURLException ignored) {
        }

        pageBacktrack = new Stack<String>();

        page = new JEditorPane();
        page.setContentType("text/html");
        page.setEditable(false);
        page.addHyperlinkListener(this);
        HTMLEditorKit ek = new HTMLEditorKit();
        page.setEditorKit(ek);
        setStyle(ek.getStyleSheet());

        JScrollPane jsp = new JScrollPane(page, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jsp, BorderLayout.CENTER);

        searchWiki(search);
    }

    public void searchWiki(String search) {
        search = search.replaceAll(" ", "_");

        try {
            page.setText(pageParser(new URL(SEARCH_URL[0] + search + SEARCH_URL[1])));
            page.setCaretPosition(0);
        } catch (Exception e) {
            page.setText("Error: " + e);
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                if (event.getDescription().equals("BACK")) {
                    pageBacktrack.pop();
                    page.setText(pageParser(new URL(pageBacktrack.pop())));
                    page.setCaretPosition(0);
                } else {
                    //page.setText(pageParser(new URL(root, event.getURL().getPath())));
                    page.setText(pageParser(new URL(event.getDescription())));
                    page.setCaretPosition(0);
                }
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

        if (url.openConnection().getContentType().contains("image")) {
            if (pageBacktrack.size() != 0) {
                pageText.append("<h1> Go back to <a href=\"BACK\">");
                String backLabel = pageBacktrack.peek().replaceAll("http://runescape.wikia.com/wiki/", "");
                backLabel = URLDecoder.decode(backLabel, "UTF-8");
                backLabel = backLabel.replaceAll("_", " ");
                pageText.append(backLabel);
                pageText.append("</a></h1>");
            }

            pageText.append("<img src=\"http://images.wikia.com");
            pageText.append(url.getPath());
            pageText.append("\"/>\n");
            pageBacktrack.push("");
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while (!finished && (line = reader.readLine()) != null) {
                if (writingBody || line.contains("<div id=\"WikiaArticle\" class=\"WikiaArticle\">")) {
                    if (!writingBody) {
                        if (pageBacktrack.size() != 0) {
                            pageText.append("<h1> Go back to <a href=\"BACK\">");
                            String backLabel = pageBacktrack.peek().replaceAll("http://runescape.wikia.com/wiki/", "");
                            backLabel = URLDecoder.decode(backLabel, "UTF-8");
                            backLabel = backLabel.replaceAll("_", " ");
                            pageText.append(backLabel);
                            pageText.append("</a></h1>");
                        }
                        writingBody = true;
                    }

                    line = line.replaceAll("<a href=\"/wiki/", "<a href=\"http://runescape.wikia.com/wiki/");
                    line = line.replaceAll("<span class=\"editsection.+</span>", "");
                    line = line.replaceAll("<figcaption.*</figcaption>", "");
                    line = line.replaceAll("<span style=\"display:none;*\">.+</span>", "");
                    line = line.replaceAll("<img", "<img border=\"0\"");

                    Matcher matcher = BACKTRACK.matcher(line);

                    if (matcher.find()) {
                        pageBacktrack.push(matcher.group(1));
                    } else {
                        pageText.append(line);
                        pageText.append("\n");
                    }

                    while (line.contains("<div")) {
                        divCount++;
                        line = line.replaceFirst("<div", "----");
                    }

                    while (line.contains("</div>")) {
                        divCount--;
                        line = line.replaceFirst("</div>", "-----");

                        if (divCount == 0) {
                            finished = true;
                        }
                    }
                }
            }

            reader.close();
        }

        pageText.append("\n</body>\n</html>");

        String out = pageText.toString();
        out = out.replaceAll("<!--[\\s\\S]+-->", "");
        out = out.replaceAll("<script>.+</script>", "");

        //System.out.println(out);
        return out;
    }

    private void setStyle(StyleSheet ss) {

        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(css.openStream()));
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