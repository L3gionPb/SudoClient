package com.sudoclient.widgets.preloaded.runescape;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 9:24 AM
 */
public final class RSClassLoader {
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("<param name=\"(.+)\" value=\"(.+)\">");
    private final HashMap<String, String> parameters = new HashMap<String, String>();
    private final File CACHE_DIR;
    private final URL baseURL;
    private final URI jarFileURI;
    private final URLClassLoader loader;

    private String gamepack;

    public RSClassLoader(File cacheDirectory) throws Exception {
        CACHE_DIR = cacheDirectory;
        baseURL = createBaseURL();
        getParameters(baseURL);
        jarFileURI = getGamePack();
        loader = new URLClassLoader(new URL[]{jarFileURI.toURL()});
    }

    private URL createBaseURL() throws MalformedURLException {
        //TODO randomize world or get redirected world
        return new URL("http://world24.runescape.com/");
    }

    private URI getGamePack() throws Exception {
        File gameJar = new File(CACHE_DIR, URLEncoder.encode(gamepack, "UTF-8"));
        URL gameJarURL = new URL(baseURL, gamepack);

        if (!gameJar.exists()) {
            File[] tempFiles = CACHE_DIR.listFiles();
            if (tempFiles != null) {
                for (File f : tempFiles) {
                    if (!f.delete()) {
                        throw new RuntimeException("Could not delete cache file: " + f.toString());
                    }
                }
            }

            if (!gameJar.createNewFile()) {
                throw new RuntimeException("Could not create cache file: " + gameJar.toString());
            }

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(gameJar));
            BufferedInputStream bis = new BufferedInputStream(gameJarURL.openStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
            final byte[] buffer = new byte[256];
            int size;

            while ((size = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, size);
            }

            baos.flush();
            bis.close();
            bos.write(baos.toByteArray());
            bos.flush();
            bos.close();
        }

        return gameJar.toURI();
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
            matcher = PARAMETER_PATTERN.matcher(s);

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
