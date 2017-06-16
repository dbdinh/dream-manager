package org.dreambot.dinh.command.commands;

import org.dreambot.dinh.command.CommandHandler;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 18:13
 */

public class Cookie extends CommandHandler {
    @Override
    public String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty() && args.length == 1) {
            StringBuilder response = new StringBuilder();
            response.append(String.format("%-47s %s", "Cookie-key", "Value")).append("\n");
            for (String cookie : cookieList.keySet()) {
                response.append(String.format("%-47s %s", cookie, cookieList.get(cookie))).append("\n");
            }
            response.setLength(response.length() - 1);
            return response.toString();
        } else {
            if (onlyContainsQuick("h") || onlyContainsFinal("help")) {
                return getHelp();
            } else if (onlyContainsQuick("a") || onlyContainsFinal("add")) {
                String value = getValue("a", "add");
                if (value == null) return "No Cookie-key was specified";
                String[] args = value.split(":");
                if (args.length == 2) {
                    Cookie.cookieList.put(args[0], args[1]);
                    return "Cookie added with key: " + args[0] + ", value: " + args[1];
                } else return "Failed to add Cookie to the Cookie-Manager";
            } else if (onlyContainsQuick("d") || onlyContainsFinal("delete")) {
                String value = getValue("d", "delete");
                if (value == null) return "No Cookie-key was specified";
                else if (!Cookie.cookieList.containsKey(value))
                    return "Could not find Cookie-key with the name '" + value + "'";
                else {
                    String reply = "Removed Cookie-key '" + value + "' with value '" + Cookie.cookieList.get(value) + "' from the Cookie-Manager";
                    Cookie.cookieList.remove(value);
                    return reply;
                }
            } else {
                return notifier;
            }
        }
    }

    @Override
    public String getDescription() {
        return "Allows you to access and modify the Cookie-manager";
    }

    @Override
    public String getHelp() {
        return "add key:value, puts the specified key and its value into the Cookie-manager\ndelete key, removes the specified key and its value from the Cookie-manager";
    }


    public static HashMap<String, String> cookieList;

    public static void handleCookies(HttpURLConnection connection) {
        HashMap<String, String> cookieList = getCookies(connection);
        for (String cookie : cookieList.keySet()) {
            Cookie.cookieList.put(cookie, cookieList.get(cookie));
        }
    }

    public static String getCookie() {
        StringBuilder cookieString = new StringBuilder();
        for (String key : cookieList.keySet()) {
            cookieString.append(key).append("=").append(cookieList.get(key)).append("; ");
        }
        return cookieString.toString().substring(0, cookieString.toString().length() - 2);
    }

    private static HashMap<String, String> getCookies(HttpURLConnection connection) {
        HashMap<String, String> cookieList = new HashMap<>();
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        Set<String> headerFieldsSet = headerFields.keySet();
        for (String headerFieldKey : headerFieldsSet) {
            System.out.println(headerFieldKey+" - "+headerFields.get(headerFieldKey));
            if ("Set-Cookie".equalsIgnoreCase(headerFieldKey)) {
                List<String> headerFieldValue = headerFields.get(headerFieldKey);
                for (String headerValue : headerFieldValue) {
                    String[] cookie = headerValue.split(";")[0].split("=");
                    cookieList.put(cookie[0], cookie[1]);
                }
            }
        }
        return cookieList;
    }
}
