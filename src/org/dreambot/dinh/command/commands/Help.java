package org.dreambot.dinh.command.commands;

import org.dreambot.dinh.command.CommandHandler;
import org.dreambot.dinh.command.Execute;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 18:05
 */

public class Help extends CommandHandler {
    @Override
    public String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty() && args.length == 1) {
            StringBuilder response = new StringBuilder();
            for (String command : Execute.commands.keySet()) {
                response
                        .append(command)
                        .append("\t")
                        .append(Execute.commands.get(command).getDescription())
                        .append("\n");
            }
            response.setLength(response.length() - 1);
            return response.toString();
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
        return "Provides a description of all available commands";
    }

    @Override
    public String getHelp() {
        return "To use this command simply enter 'help'";
    }
}
