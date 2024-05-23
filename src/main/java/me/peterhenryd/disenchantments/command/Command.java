package me.peterhenryd.disenchantments.command;

import me.peterhenryd.disenchantments.Disenchantments;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public abstract class Command implements TabCompleter, CommandExecutor {
    protected final Disenchantments plugin;
    private final String name;

    public Command(Disenchantments plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public boolean register() {
        PluginCommand command = plugin.getCommand(name);

        if (command == null) {
            return false;
        }

        command.setExecutor(this);
        command.setTabCompleter(this);

        return true;
    }

    public final String getName() {
        return name;
    }
}
