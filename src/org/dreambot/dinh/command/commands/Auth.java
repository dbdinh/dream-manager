package org.dreambot.dinh.command.commands;

import org.dreambot.dinh.command.CommandHandler;
import org.dreambot.dinh.website.WebsiteContext;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

/**
 * Created by: Niklas
 * Date: 14.06.2017
 * Time: 01:17
 */

public class Auth extends CommandHandler {
    @Override
    protected String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty()) {
            return notifier;
        } else {
            if (onlyContainsQuick("h") || onlyContainsFinal("help")) {
                return getHelp();
            } else if (containsQuick("i", "t") && containsFinal("username")) {
                String username = getValue("u", "username");
                String script = getValue("i", "id");
                String time = getValue("t", "time");
                if (username == null || script == null || time == null) return notifier;
                boolean authorized = authorizeUser(username.replaceAll(" ", "+"), script, time);
                if (authorized)
                    return "Successfully authorized " + username + " for using: " + script + ", " + time + " hours";
                else return "Failed to authorize user " + username + " for script " + script;
            } else {
                return notifier;
            }
        }
    }

    private boolean authorizeUser(String username, String scriptID, String duration) {
        try {
            HttpsURLConnection connection = WebsiteContext.getConnection(new URL("https://dreambot.org/sdn/scripterpanel-authorize.php"), "https://dreambot.org/sdn/scripterpanel-authorize.php", "POST");

            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(WebsiteContext.getPostData("authid=" + username, "scriptid=" + scriptID, "expiretime=" + duration, "add=true"));
            out.flush();

            InputStream stream = WebsiteContext.urlExists(connection);
            if (stream == null) {
                return false;
            }

            Cookie.handleCookies(connection);
            if (connection.getResponseCode() != 403 && connection.getInputStream() != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
                bufferedReader.close();
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Allows you to authorize a user for your Script";
    }

    @Override
    public String getHelp() {
        return "u\tusername\ns\tscript-id\nt\thours";
    }
}
