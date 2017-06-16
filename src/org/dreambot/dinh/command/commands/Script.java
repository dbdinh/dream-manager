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
 * Time: 01:35
 */

public class Script extends CommandHandler {

    private String cachedId;

    @Override
    protected String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty() && args.length == 1) {
            if (cachedId != null) return cachedId;
            return getScript();
        } else {
            if (onlyContainsQuick("h") || onlyContainsFinal("help")) {
                return getHelp();
            } else if (onlyContainsQuick("c") || onlyContainsFinal("clear")) {
                cachedId = null;
                return "Successfully cleared the cached scripts";
            } else {
                return notifier;
            }
        }
    }

    private String getScript() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpsURLConnection connection = WebsiteContext.getConnection(new URL("https://dreambot.org/sdn/scripterpanel-overview.php"), "https://dreambot.org/sdn/scripterpanel-overview.php", null);
            Cookie.handleCookies(connection);

            if (connection.getResponseCode() != 403 && connection.getInputStream() != null) {
                InputStream stream = WebsiteContext.urlExists(connection);
                if (stream == null) {
                    return "Could not find scripts";
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                stringBuilder.append(String.format("%-3s %-23s %-15s %s", "ID", "Name", "Category", "Type").trim()).append("\n");
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("even") || line.contains("odd")) {
                        String[] requestArray = line.split("td class=");
                        for (int i = 0; i < requestArray.length; i += 6) {
                            String ID = requestArray[1].substring(requestArray[1].indexOf(">") + 1, requestArray[1].indexOf("<"));
                            String name = requestArray[2].substring(requestArray[2].indexOf(">") + 1, requestArray[2].indexOf("<"));
                            String category = requestArray[3].substring(requestArray[3].indexOf(">") + 1, requestArray[3].indexOf("<"));
                            String type = requestArray[4].substring(requestArray[4].indexOf(">") + 1, requestArray[4].indexOf("<"));
                            stringBuilder.append(String.format("%s %-23s %-15s %s", ID, name, category, type).trim()).append("\n");
                        }
                    }
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Could not find scripts";
        }

        stringBuilder.setLength(stringBuilder.length() - 1);
        cachedId = stringBuilder.toString();
        return cachedId;
    }

    @Override
    public String getDescription() {
        return "Provides a list of your current script-ids";
    }

    @Override
    public String getHelp() {
        return "clear, clears the cached result\nTo use this command simply enter 'script'";
    }
}
