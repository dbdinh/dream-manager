package org.dreambot.dinh.command;

import org.dreambot.dinh.command.commands.*;

import java.util.HashMap;

public class Execute {

    public static HashMap<String, CommandHandler> commands = new HashMap<>();

    public Execute() {
        commands.put("help", new Help());
        commands.put("cookie", new Cookie());
        commands.put("sale", new Sales());
        commands.put("auth", new Auth());
        commands.put("script", new Script());
        commands.put("request", new Requests());
        commands.put("bump", new Bump());
        commands.put("refer", new Refer());
        commands.put("time", new Time());
    }

    public String executeCommand(String command) {
        if (commands.containsKey(command.split(" ")[0])) {
            return commands.get(command.split(" ")[0]).performCommand(command);
        }
        return command.split(" ")[0] + " is not a command";
    }
}
