package org.dreambot.dinh;

import org.dreambot.dinh.command.commands.Cookie;
import org.dreambot.dinh.console.Console;
import org.dreambot.dinh.website.WebsiteContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 01:06
 */

public class DreamBot extends Console {

    public static String authKey;

    public static void main(String[] args) {
        new DreamBot("Console");
    }

    private DreamBot(String title) {
        super(title);
    }

    public static boolean login(String username, String password) {
        authKey = getAuthKey();
        if (authKey == null) return false;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://dreambot.org/forums/index.php?app=core&module=global&section=login&do=process").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(WebsiteContext.getPostData("auth_key=" + authKey, "ips_username=" + username, "ips_password=" + password, "rememberMe=1"));
            out.flush();

            Cookie.cookieList = new HashMap<>();
            Cookie.handleCookies(connection);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("Your Profile")) {
                    Cookie.cookieList.put("dream_member_id", line.replaceAll("[^\\d+]", ""));
                    bufferedReader.close();
                    return true;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getAuthKey() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://dreambot.org/forums/index.php?app=core&module=global&section=login").openConnection();
            connection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("auth_key")) {
                    String authKey = line.split("value=")[1].split(" ")[0].replaceAll("'", "");
                    bufferedReader.close();
                    return authKey;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
