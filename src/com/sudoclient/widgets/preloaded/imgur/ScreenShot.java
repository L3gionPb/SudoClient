package com.sudoclient.widgets.preloaded.imgur;

import com.sudoclient.client.components.ClientMenu;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: deprecated
 * Date: 4/27/12
 * Time: 8:39 PM
 */
public class ScreenShot {
    private final static Pattern IMAGE_URL_PATTERN = Pattern.compile("<original>(.+)</original>");
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH:mm");
    private final static String API_KEY = "10ff513c98f16f42e5dc1c2f305eb985";
    private final static BASE64Encoder ENCODER = new BASE64Encoder();
    private final static char FS = File.separatorChar;
    private static Robot robot = null;

    public static void takeScreenShot(final Rectangle bounds, final ClientMenu ctx) {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedImage screenShot;
                if (ctx.isFullScreen()) {
                    screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                } else {
                    screenShot = robot.createScreenCapture(bounds);
                }
                saveScreenShot(screenShot);
                String url = upload(screenShot);
                copyToClipboard(url);
                ctx.screenShotFinished();
            }
        }).start();
    }

    private static void saveScreenShot(BufferedImage image) {
        File folder = new File(System.getProperty("user.home") + FS + "Pictures" + FS + "RSScreenShots");

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new RuntimeException("Could not make screenshot directory: " + folder.toString());
            }
        }

        Date date = new Date(System.currentTimeMillis());
        File f = new File(folder, "SS-" + DATE_FORMAT.format(date) + ".png");

        try {
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    throw new RuntimeException("Could not create screenshot file: " + f.toString());
                }
            }

            ImageIO.write(image, "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String upload(BufferedImage image) {
        try {
            URL uploadURL = new URL("http://api.imgur.com/2/upload");

            URLConnection connection = uploadURL.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            String data = URLEncoder.encode("image", "UTF-8") + "=" +
                    URLEncoder.encode(ENCODER.encode(getImageByteArray(image)), "UTF-8");
            data += "&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8");

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String outURL = "ERROR: Upload failed";
            reader.readLine();
            Matcher matcher = IMAGE_URL_PATTERN.matcher(reader.readLine());

            if (matcher.find()) {
                outURL = matcher.group(1);
            }

            reader.close();
            return outURL;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ERROR: Upload failed";
    }

    private static byte[] getImageByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    private static void copyToClipboard(String url) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection strSel = new StringSelection(url);
        clipboard.setContents(strSel, null);
    }
}
