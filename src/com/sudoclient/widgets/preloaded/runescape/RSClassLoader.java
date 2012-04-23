package com.sudoclient.widgets.preloaded.runescape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 9:24 AM
 */
public class RSClassLoader {
    private URLClassLoader loader;
    private final HashMap<String, String> parameters = new HashMap<String, String>();
    private static final Pattern PARAMETER_PATTER = Pattern.compile("<param name=\"(.+)\" value=\"(.+)\">");
    private URL baseURL;
    private String gamepack;

    public RSClassLoader() {
        load();
    }

    private void loadJarClasses() throws MalformedURLException {
        loader = new URLClassLoader(new URL[]{new URL(baseURL, gamepack)});
    }

    private void setBaseURL() throws MalformedURLException {
        baseURL = new URL("http://world24.runescape.com/");
    }

    public void load() {
        try {
            setBaseURL();
            getParameters(baseURL);
            loadJarClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getParameters(URL url) throws IOException, InterruptedException {
        URLConnection uc = url.openConnection();
        uc.addRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        uc.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        uc.addRequestProperty("Accept-Encoding", "gzip,deflate");
        uc.addRequestProperty("Accept-Language", "en-gb,en;q=0.5");
        uc.addRequestProperty("Connection", "keep-alive");
        uc.addRequestProperty("Host", "www.runescape.com");
        uc.addRequestProperty("Keep-Alive", "300");
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.8.0.6) Gecko/20060728 Firefox/1.5.0.6");

        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String s;
        Matcher matcher;

        while ((s = br.readLine()) != null) {
            matcher = PARAMETER_PATTER.matcher(s);

            while (matcher.find()) {
                parameters.put(matcher.group(1), matcher.group(2));
            }

            if (s.contains("document.write('archive")) {
                gamepack = s.substring(24, s.lastIndexOf(" '"));
            }
        }

        br.close();
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loader.loadClass(name);
    }

    public URL getBaseURL() {
        return baseURL;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }
}
