package me.peterhenryd.disenchantments;

import me.peterhenryd.disenchantments.command.DisenchantCommand;
import me.peterhenryd.disenchantments.command.DisenchantmentsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Disenchantments extends JavaPlugin {
    private static Disenchantments INSTANCE = null;

    private Locale locale;
    private DisenchantCommand disenchantCommand;
    private DisenchantmentsCommand disenchantmentsCommand;

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();

        locale = new Locale(this);
        disenchantCommand = new DisenchantCommand(this);
        disenchantmentsCommand = new DisenchantmentsCommand(this);

        if (!disenchantCommand.register()) {
            getLogger().severe("Failed to register the '/disenchant' command!");
        }

        if (!disenchantmentsCommand.register()) {
            getLogger().severe("Failed to register the '/disenchantments' command!");
        }
    }

    @Override
    public void onDisable() {

    }

    public Locale getLocale() {
        return locale;
    }

    public DisenchantCommand getDisenchantCommand() {
        return disenchantCommand;
    }

    public DisenchantmentsCommand getDisenchantmentsCommand() {
        return disenchantmentsCommand;
    }

    public static Disenchantments getInstance() {
        return INSTANCE;
    }
}
