package com.sudoclient.widgets.preloaded.runewikia;

import javax.swing.text.JTextComponent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: deprecated
 * Date: 4/27/12
 * Time: 1:34 PM
 */
public class PageLoader implements Runnable {
    private static final Pattern BACKTRACK = Pattern.compile("Retrieved from \"<a href=\"(.+)\">.+</a>\"");
    private boolean alive;
    private URL url;
    private JTextComponent output;
    private String back, finalizedURL;

    public PageLoader(String url, String back, JTextComponent output) {
        this.back = back;
        this.output = output;
        alive = true;

        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            this.output.setText("Error opening: " + url + "\n" + e.getMessage());
            this.url = null;
        }
    }

    public void kill() {
        alive = false;
    }

    public void onFinish() {
    }

    public String getFinalizedURL() {
        return finalizedURL;
    }

    @Override
    public void run() {
        if (url == null) {
            return;
        }

        StringBuilder pageText = new StringBuilder();
        boolean writingBody = false, finished = false;
        int divCount = 0;
        pageText.append("<html>\n<head>\n<title> RuneWikia </title>\n</head>\n<body>\n");

        try {
            if (url.openConnection().getContentType().contains("image")) {
                if (back != null) {
                    pageText.append("<h1> Go back to <a href=\"BACK\">");
                    String backLabel = back.substring(back.contains("/") ? back.lastIndexOf("/") + 1 : 0);
                    backLabel = URLDecoder.decode(backLabel, "UTF-8");
                    backLabel = backLabel.replaceAll("_", " ");
                    pageText.append(backLabel);
                    pageText.append("</a></h1>");
                }

                pageText.append("<img src=\"http://images.wikia.com");
                pageText.append(url.getPath());
                pageText.append("\"/>\n");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;

                while (alive && !finished && (line = reader.readLine()) != null) {
                    if (writingBody || line.contains("<div id=\"WikiaArticle\" class=\"WikiaArticle\">")) {
                        if (!writingBody) {
                            if (back != null) {
                                pageText.append("<h1> Go back to <a href=\"BACK\">");
                                String backLabel = back.substring(back.contains("/") ? back.lastIndexOf("/") + 1 : 0);
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
                            finalizedURL = matcher.group(1);
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
        } catch (IOException e) {
            pageText.append("Error loading: " + url + "\n" + e.getMessage());
        }

        pageText.append("\n</body>\n</html>");

        String out = pageText.toString();
        out = out.replaceAll("<!--[\\s\\S]+-->", "");
        out = out.replaceAll("<script>.+</script>", "");

        if (alive) {
            output.setText(out);
            output.setCaretPosition(0);
            onFinish();
        }
    }
}