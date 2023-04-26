package fr.nimelia.gameapi.utils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.util.Map;

public class CommandUtils {
    private CommandUtils() {}

    /**
    * @param fallbackPrefix It's probably your plugin's name
    */
    public static void registerCommand(String fallbackPrefix, Command command) {
        ((CraftServer) Bukkit.getServer()).getCommandMap().register(fallbackPrefix, command);
    }

    /**
    * @param fallbackPrefix It's probably your plugin's name
    */
    public static void unregisterCommand(String fallbackPrefix, Command command) throws ReflectiveOperationException {
        SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
        Map<String, Command> commands = (Map<String, Command>) FieldUtils.getDeclaredField(SimpleCommandMap.class, "knownCommands", true).get(commandMap);
        Command removedCommand = commands.remove(fallbackPrefix + ":" + command.getName());
        if(removedCommand == command) {
            removedCommand.unregister(commandMap);
        }
        removedCommand = commands.remove(command.getName());
        if(removedCommand == command) {
            removedCommand.unregister(commandMap);
        }
    }
}