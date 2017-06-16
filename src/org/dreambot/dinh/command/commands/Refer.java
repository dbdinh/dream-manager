package org.dreambot.dinh.command.commands;

import org.dreambot.dinh.command.CommandHandler;

/**
 * Created by: Niklas
 * Date: 14.06.2017
 * Time: 14:42
 */

public class Refer extends CommandHandler {
    @Override
    public String performCommand(String input) {
        super.setData(input);
        if (singleList.isEmpty() && doubleList.isEmpty() && args.length == 1) {
            return "https://dreambot.org/forums/index.php?app=referrals&reff=" + Cookie.cookieList.get("dream_member_id");
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
        return "Provides you with your ref link";
    }

    @Override
    public String getHelp() {
        return "To use this command simply enter 'refer'";
    }
}
