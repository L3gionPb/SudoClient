package com.sudoclient.widgets.api.players.attributes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: deprecated
 * Date: 4/29/12
 * Time: 2:56 AM
 */

public final class RSFeed implements Runnable {
    private final static ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1);
    private final static String BASE_FEED_URL = "http://services.runescape.com/m=adventurers-log/rssfeed?searchName=";
    private static DocumentBuilder XML_BUILDER = null;
    private final ArrayList<FeedEntry> feedEntries;
    private final ArrayList<RSFeedListener> listeners;
    private URL url;

    public RSFeed(final String username) {
        feedEntries = new ArrayList<FeedEntry>();
        listeners = new ArrayList<RSFeedListener>();

        try {
            if (XML_BUILDER == null) {
                XML_BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            }

            url = new URL(BASE_FEED_URL + URLEncoder.encode(username, "UTF-8"));
        } catch (Exception ignored) {
        }

        EXECUTOR.scheduleWithFixedDelay(this, 0, 5, TimeUnit.MINUTES);
    }

    public final ArrayList<FeedEntry> getFeedEntries() {
        return feedEntries;
    }

    public final void addListener(final RSFeedListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final RSFeedListener listener) {
        listeners.remove(listener);
    }

    public final void remove() {
        EXECUTOR.remove(this);
    }

    private void refresh() {
        try {
            Document doc = XML_BUILDER.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    FeedEntry entry = FeedEntryFactory.define(element);

                    if (!feedEntries.contains(entry)) {
                        feedEntries.add(0, entry);

                        for (RSFeedListener listener : listeners) {
                            listener.feedEntryReceived(this, entry);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void run() {
        refresh();
    }

    public static void killAll() {
        EXECUTOR.shutdownNow();
    }

    private final static class FeedEntryFactory {
        private static FeedEntry define(final Element element) {
            String title = getTagValue("title", element);
            String desc = getTagValue("description", element);
            String idString = getTagValue("link", element);

            return new FeedEntry(title, desc, Integer.parseInt(idString.substring(idString.lastIndexOf("=") + 1)));
        }

        private static String getTagValue(final String sTag, final Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
            Node nValue = nlList.item(0);
            return nValue.getNodeValue();
        }
    }

    public final static class FeedEntry {
        private final String title, desc;
        private final int id;

        private FeedEntry(final String title, final String desc, final int id) {
            this.title = title;
            this.desc = desc;
            this.id = id;
        }

        public final String getTitle() {
            return title;
        }

        public final String getDesc() {
            return desc;
        }

        @Override
        public final boolean equals(Object o) {
            return (o instanceof FeedEntry) && (((FeedEntry) o).id == id);
        }
    }

    public static interface RSFeedListener {
        public void feedEntryReceived(RSFeed source, FeedEntry entry);
    }
}
