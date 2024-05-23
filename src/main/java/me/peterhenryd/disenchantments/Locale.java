package me.peterhenryd.disenchantments;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Locale {
    private final Disenchantments plugin;
    private final HashMap<String, String> messages;

    public Locale(Disenchantments plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();

        reload();
    }

    public void reload() {
        ConfigurationSection localeSection = plugin.getConfig().getConfigurationSection("locale");

        if (localeSection == null) {
            throw new NullPointerException("Could not load 'locale' section in config.yml");
        }

        Set<String> keys = localeSection.getKeys(false);

        for (String key : keys) {
            Object value = localeSection.get(key);

            String string = null;
            if (value instanceof List<?> list) {
                List<String> stringList = new ArrayList<>();
                for (Object o : list) {
                    stringList.add(o.toString());
                }

                string = String.join("\n", stringList);
            } else if (value instanceof String s) {
                string = s;
            }

            if (string == null) {
                throw new NullPointerException("Could not load 'locale." + key + "' in config.yml");
            }

            for (String existingKey : messages.keySet()) {
                string = string.replaceAll("\\{" + existingKey + "}", messages.get(existingKey));
            }

            messages.put(key, string);
        }
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.get(key));
    }
}
