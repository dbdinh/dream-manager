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
 * Time: 02:12
 */

public class Requests extends CommandHandler {

    private String cacheRequest;

    @Override
    protected String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty() && args.length == 1) {
            if (cacheRequest != null) return cacheRequest;
            return getRequest(0);
        } else {
            if (onlyContainsQuick("h") || onlyContainsFinal("help")) {
                return getHelp();
            } else if (onlyContainsQuick("c") || onlyContainsFinal("clear")) {
                cacheRequest = null;
                return "Successfully cleared the cached IDs";
            } else if (onlyContainsQuick("r") || onlyContainsFinal("recent")) {
                String value = getValue("r", "recent");
                if (value == null || !value.matches("^[0-9]+")) return "Recent value has to be a positive Integer";
                return getRequest(Integer.parseInt(value));
            } else {
                return notifier;
            }
        }
    }

    private String getRequest(int startIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpsURLConnection connection = WebsiteContext.getConnection(new URL("https://dreambot.org/sdn/scripterpanel-requests.php"), "https://dreambot.org/sdn/scripterpanel-requests.php", "GET");
            Cookie.handleCookies(connection);

            InputStream stream = WebsiteContext.urlExists(connection);
            if (stream == null) {
                return "Could not find requests";
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            stringBuilder.append(String.format("%-4s %-35s %-19s %s", "ID", "Name", "Timestamp", "Result").trim()).append("\n");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("timestamp")) {
                    String[] requestArray = line.split("<tr class");
                    for (int i = (startIndex == 0) ? 0 : requestArray.length - startIndex; i < requestArray.length; i++) {
                        String object = requestArray[i];
                        if (!(object.contains("odd") || object.contains("even"))) continue;
                        String[] objectInfo = object.split("class=");
                        String requestID = objectInfo[1].substring(objectInfo[1].indexOf(">") + 1, objectInfo[1].indexOf("<"));
                        String name = objectInfo[2].substring(objectInfo[2].indexOf(">") + 1, objectInfo[2].indexOf("<"));
                        String timestamp = objectInfo[3].substring(objectInfo[3].indexOf(">") + 1, objectInfo[3].indexOf("<"));
                        String result = objectInfo[4].substring(objectInfo[4].indexOf(">") + 1, objectInfo[4].indexOf("<"));
                        String comment = objectInfo[5].substring(objectInfo[5].indexOf(">") + 1, objectInfo[5].indexOf("<"));
                        stringBuilder.append(String.format("%s %-35s %2s %-10s " + (comment.equals("") ? "%s" : "\n%1$s %5$s"), requestID, name, timestamp, result, comment).trim()).append("\n");
                    }
                    stringBuilder.setLength(stringBuilder.length() - 1);
                    bufferedReader.close();
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (startIndex == 0) cacheRequest = stringBuilder.toString();
        return stringBuilder.toString();
    }

    @Override
    public String getDescription() {
        return "Provides a list of your current requests";
    }

    @Override
    public String getHelp() {
        return "clear, clears the cached requests\nrecent, only prints the last %r requests\nTo use this command simply enter 'request'";
    }
}
