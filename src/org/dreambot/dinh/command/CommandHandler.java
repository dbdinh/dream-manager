package org.dreambot.dinh.command;

import java.util.HashMap;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 01:15
 */

public abstract class CommandHandler {

    protected final String notifier = "wrong use of arguments, please check your command";

    protected String[] args;
    protected HashMap<String, String> doubleList;
    protected HashMap<String, String> singleList;

    protected String setData(String input) {
        this.args = input.split(" ");
        this.doubleList = new HashMap<>();
        this.singleList = new HashMap<>();
        loop:
        for (int i = 1; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2) {
                        return "command '" + args[0] + "' missing argument!";
                    }
                    if (args[i].charAt(1) == '-') {
                        doubleList.put(args[i].substring(2), (i + 1 >= args.length) ? null : getLeft(args, i + 1));
                        break loop;
                    } else {
                        singleList.put(args[i].substring(1), (i + 1 >= args.length) ? null : args[i + 1]);
                    }
                    break;
                default:
                    break;
            }
        }
        return input;
    }

    private String getLeft(String[] args, int index) {
        String left = "";
        for (int i = index; i < args.length; i++) {
            left += args[i] + " ";
        }
        return left.trim();
    }

    protected boolean onlyContainsQuick(String... argument) {
        if (singleList.size() != argument.length || doubleList.size() != 0) return false;
        for (String arg : argument) {
            if (!singleList.containsKey(arg)) return false;
        }
        return true;
    }

    protected boolean onlyContainsFinal(String... argument) {
        if (doubleList.size() != argument.length || singleList.size() != 0) return false;
        for (String arg : argument) {
            if (!doubleList.containsKey(arg)) return false;
        }
        return true;
    }

    protected boolean containsQuick(String... argument) {
        if (singleList.size() != argument.length) return false;
        for (String arg : argument) {
            if (!singleList.containsKey(arg)) return false;
        }
        return true;
    }

    protected boolean containsFinal(String... argument) {
        if (doubleList.size() != argument.length) return false;
        for (String arg : argument) {
            if (!doubleList.containsKey(arg)) return false;
        }
        return true;
    }

    protected String getValue(String quick, String info) {
        return singleList.containsKey(quick) ? singleList.get(quick) : doubleList.get(info);
    }

    protected abstract String performCommand(String input);

    public abstract String getDescription();

    public abstract String getHelp();
}
