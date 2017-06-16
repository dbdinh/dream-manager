package org.dreambot.dinh.website;

import org.dreambot.dinh.command.commands.Cookie;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 18:04
 */

public class WebsiteContext {

    public static String getPostData(String... s) {
        return String.join("&", s);
    }

    private static final Object GZIP_CONTENT_TYPE = "gzip";

    public static InputStream urlExists(HttpsURLConnection con) {
        try {
            if (GZIP_CONTENT_TYPE.equals(con.getContentEncoding())) {
                return new GZIPInputStream(con.getResponseCode() != 200 ? con.getErrorStream() : con.getInputStream());
            }
            return con.getResponseCode() != 200 ? con.getErrorStream() : con.getInputStream();
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static HttpsURLConnection getConnection(URL url, String topicURL, String request) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        if (request != null) connection.setRequestMethod(request);
        connection.setRequestProperty("Host", "dreambot.org");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("Referer", topicURL);
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
        connection.setRequestProperty("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
        connection.setRequestProperty("Cookie", Cookie.getCookie());
        return connection;
    }

}
