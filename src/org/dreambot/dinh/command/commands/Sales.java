package org.dreambot.dinh.command.commands;

import org.dreambot.dinh.command.CommandHandler;
import org.dreambot.dinh.website.WebsiteContext;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 18:35
 */

public class Sales extends CommandHandler {
    @Override
    protected String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty() && args.length == 1) {
            return getSale();
        } else {
            if (onlyContainsQuick("h") || onlyContainsFinal("help")) {
                return getHelp();
            } else {
                return notifier;
            }
        }
    }

    private String getSale() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpsURLConnection connection = WebsiteContext.getConnection(new URL("https://dreambot.org/sdn/scripter-scriptinfo.php"), "https://dreambot.org/sdn/scripter-scriptinfo.php", null);
            Cookie.handleCookies(connection);
            if (connection.getResponseCode() != 403 && connection.getInputStream() != null) {
                InputStream stream = WebsiteContext.urlExists(connection);
                if (stream == null) {
                    return "Could not find sales";
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("<tr class=\"even\">")) {
                        String[] saleInformation = line.split("<td>");
                        List<List<String>> itemList = new ArrayList<>();
                        int longestName = 0;
                        for (int i = 1; i < saleInformation.length; i += 3) {
                            String name = saleInformation[i].trim().replaceAll("<.*?>", "");
                            String sales = saleInformation[i + 1].trim().replaceAll("<.*?>", "");
                            String total = saleInformation[i + 2].trim().replaceAll("<.*?>", "");
                            if (name.length() > longestName) longestName = name.length();
                            itemList.add(Arrays.asList(name, sales, total));
                        }
                        stringBuilder.append(String.format("%-" + (longestName + 5) + "s %-20s %s ", "Name", "Sales", "Owed")).append("\n");
                        for (List<String> saleObject : itemList) {
                            stringBuilder.append(String.format("%-" + (longestName + 5) + "s %-20s %s ", saleObject.get(0), saleObject.get(1), saleObject.get(2))).append("\n");
                        }
                        stringBuilder.setLength(stringBuilder.length() - 1);
                        bufferedReader.close();
                        break;
                    }
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Could not find sales";
        }
        return stringBuilder.toString();
    }

    @Override
    public String getDescription() {
        return "Provides a list of current sales";
    }

    @Override
    public String getHelp() {
        return "To use this command simply enter 'sale'";
    }
}
