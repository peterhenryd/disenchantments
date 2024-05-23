package me.peterhenryd.disenchantments.command;

import me.peterhenryd.disenchantments.Disenchantments;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DisenchantCommand extends Command {
    public DisenchantCommand(Disenchantments plugin) {
        super(plugin, "disenchant");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] arguments) {
        if (!sender.hasPermission("disenchantments.use")) {
            sender.sendMessage(plugin.getLocale().getMessage("no-permission"));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getLocale().getMessage("must-be-player"));
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            sender.sendMessage(plugin.getLocale().getMessage("no-item"));
        }

        if (itemStack.getEnchantments().isEmpty()) {
            sender.sendMessage(plugin.getLocale().getMessage("no-enchantments"));
            return true;
        }

        if (inventory.firstEmpty() == -1) {
            sender.sendMessage(plugin.getLocale().getMessage("inventory-full"));
            return true;
        }

        if (!inventory.contains(Material.BOOK)) {
            sender.sendMessage(plugin.getLocale().getMessage("no-books"));
            return true;
        }

        if (arguments.length == 0) {
            sender.sendMessage(plugin.getLocale().getMessage("enchantment-unspecified"));
            return true;
        }

        String enchantmentString = arguments[0];

        Enchantment enchantment = null;
        for (Enchantment e : Registry.ENCHANTMENT) {
            if (!enchantmentString.equalsIgnoreCase(e.getKey().getKey())
                    && !enchantmentString.equalsIgnoreCase(e.getKey().toString())) {
                continue;
            }

            enchantment = e;
            break;
        }

        if (enchantment == null) {
            sender.sendMessage(plugin.getLocale().getMessage("unknown-enchantment"));
            return true;
        }

        if (!itemStack.getEnchantments().containsKey(enchantment)) {
            sender.sendMessage(plugin.getLocale().getMessage("non-applicable-enchantment"));
            return true;
        }

        int enchantmentLevel = itemStack.getEnchantmentLevel(enchantment);

        itemStack.removeEnchantment(enchantment);

        int bookIndex = inventory.first(Material.BOOK);
        ItemStack book = Objects.requireNonNull(inventory.getItem(bookIndex));

        if (book.getAmount() == 1) {
            inventory.remove(book);
        } else {
            book.setAmount(book.getAmount() - 1);
            inventory.setItem(bookIndex, book);
        }

        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK, 1);

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
        meta.addStoredEnchant(enchantment, enchantmentLevel, true);

        enchantedBook.setItemMeta(meta);
        inventory.addItem(enchantedBook);

        player.sendMessage(plugin.getLocale().getMessage("disenchantment-successful"));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return null;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();

        List<String> names = new ArrayList<>();
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            names.add(enchantment.getKey().getKey());
        }

        return names;
    }
}
