package me.peterhenryd.disenchantments.command;

import me.peterhenryd.disenchantments.Disenchantments;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DisenchantmentsCommand extends Command {
    public DisenchantmentsCommand(Disenchantments plugin) {
        super(plugin, "disenchantments");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] arguments) {
        if (!sender.hasPermission("disenchantments.admin")) {
            sender.sendMessage(plugin.getLocale().getMessage("no-permission"));
            return true;
        }

        if (arguments.length == 0) {
            sender.sendMessage(plugin.getLocale().getMessage("disenchantments-overview")
                    .replace("{0}", plugin.getDescription().getVersion()));
            return true;
        }

        String subcommand = arguments[0];

        if (subcommand.equalsIgnoreCase("reload")) {
            sender.sendMessage(plugin.getLocale().getMessage("disenchantments-reload"));
            plugin.reloadConfig();
            plugin.getLocale().reload();

            return true;
        }

        sender.sendMessage(plugin.getLocale().getMessage("unknown-subcommand"));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        return List.of("reload");
    }
}
