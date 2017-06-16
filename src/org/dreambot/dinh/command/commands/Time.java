package org.dreambot.dinh.command.commands;

import org.dreambot.dinh.command.CommandHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by: Niklas
 * Date: 14.06.2017
 * Time: 20:31
 */

public class Time extends CommandHandler {
    @Override
    public String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty() && args.length == 1) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return dateFormat.format(new Date());
        } else {
            if (onlyContainsQuick("h") || onlyContainsFinal("help")) {
                return getHelp();
            } else {
                return notifier;
            }
        }
    }

    @Override
    public String getDescription() {
        return "Provides you with your current date and time";
    }

    @Override
    public String getHelp() {
        return "To use this command simply enter 'time'";
    }
}
