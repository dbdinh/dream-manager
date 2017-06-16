package org.dreambot.dinh.command.commands;

import org.dreambot.dinh.command.CommandHandler;
import org.dreambot.dinh.website.WebsiteContext;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by: Niklas
 * Date: 14.06.2017
 * Time: 13:31
 */

public class Bump extends CommandHandler {
    @Override
    protected String performCommand(String input) {
        super.setData(input);
        if (onlyContainsQuick("i") || onlyContainsFinal("id")) {
            String value = getValue("i", "id");
            if (value == null || !value.matches("^[0-9]+")) return "ID value has to be a positive Integer";
            String threadURL = getThread(value);
            if (threadURL == null) return "Could not fetch Thread-URL of script: '" + value + "'";
            return bumpTopic(threadURL);
        } else {
            if (onlyContainsQuick("h") || onlyContainsFinal("help")) {
                return getHelp();
            } else {
                return notifier;
            }
        }
    }

    private String bumpTopic(String topicURL) {
        try {
            HttpsURLConnection connection = WebsiteContext.getConnection(new URL(topicURL + "?bumpTopic=1"), topicURL, "GET");
            Cookie.handleCookies(connection);

            InputStream stream = WebsiteContext.urlExists(connection);
            if (stream == null) {
                return "Failed to bump topic";
            }


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("wait")) {
                    bufferedReader.close();
                    return line.trim();
                } else if (line.contains("Topic bumped")) return "Topic bumped";
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Failed to bump topic";
    }

    private String getThread(String scriptID) {
        try {
            HttpsURLConnection connection = WebsiteContext.getConnection(new URL("https://dreambot.org/sdn/scripterpanel-editscript.php?scriptid=" + scriptID), "https://dreambot.org/sdn/scripterpanel-overview.php", null);
            Cookie.handleCookies(connection);
            if (connection.getResponseCode() != 403 && connection.getInputStream() != null) {
                InputStream stream = WebsiteContext.urlExists(connection);
                if (stream == null) {
                    return null;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("threadurl")) {
                        return line.split("value=\"")[1].split("\"")[0];
                    }
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows you to bump one of your script Threads";
    }

    @Override
    public String getHelp() {
        return "id\tscript-id";
    }
}
