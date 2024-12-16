package xyz.motz.randomizer.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import xyz.motz.randomizer.main.Randomizer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ShuffleCommand implements CommandExecutor {

    private List<?> blacklist;
    private File itemsFile;
    private FileConfiguration itemsConfig;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Randomizer plugin = Randomizer.getPlugin();
        Randomizer.getPlugin().remaining.clear();


        if (plugin.itemsConfig == null) {
            plugin.initializeItemsConfig();
        }

        FileConfiguration itemsConfig = plugin.itemsConfig;
        
        itemsConfig.set("blocks", null);
        itemsConfig.set("mobs", null);
        itemsConfig.set("items", null);


        plugin.remaining.clear();
        for (Material mat : Material.values()) {
            blacklist = plugin.getConfig().getList("blacklist");
            if (mat.isItem() && !blacklist.contains(mat)) {
                plugin.remaining.add(mat);
            }
        }

        for (Material mat : Material.values()) {
            if (mat.isBlock() && !plugin.remaining.isEmpty()) {
                Random r = new Random();
                int rand = plugin.remaining.size() > 1
                        ? r.nextInt(plugin.remaining.size() - 1)
                        : 0;

                itemsConfig.set("blocks." + mat, plugin.remaining.get(rand).toString());
                plugin.remaining.remove(rand);
            }
        }

        //  ITEM SHUFFLE FOR CRAFTING (ITEMS)
        plugin.remaining.clear();
        for (Material mat : Material.values()) {
            blacklist = plugin.getConfig().getList("blacklist");
            if (mat.isItem() && !blacklist.contains(mat)) {
                plugin.remaining.add(mat);
            }
        }

        for (Material mat : Material.values()) {
            if (mat.isItem() && !plugin.remaining.isEmpty()) {
                Random r = new Random();
                int rand = plugin.remaining.size() > 1
                        ? r.nextInt(plugin.remaining.size() - 1)
                        : 0;

                itemsConfig.set("items." + mat, plugin.remaining.get(rand).toString());
                plugin.remaining.remove(rand);
            }
        }

        // SHUFFLE IT FOR MOBS
        plugin.remainingmobs.clear();

        plugin.remainingmobs.clear();
        for (Material mat : Material.values()) {
            blacklist = plugin.getConfig().getList("blacklist");
            if (mat.isItem() && !blacklist.contains(mat)) {
                plugin.remainingmobs.add(mat);
            }
        }

        for (EntityType mob : EntityType.values()) {
            if (!plugin.remainingmobs.isEmpty()) {
                Random r = new Random();
                int rand = plugin.remainingmobs.size() > 1
                        ? r.nextInt(plugin.remainingmobs.size() - 1)
                        : 0;

                itemsConfig.set("mobs." + mob, plugin.remainingmobs.get(rand).toString());
                plugin.remainingmobs.remove(rand);
            }
        }

        try {
            itemsConfig.save(plugin.itemsFile);
            sender.sendMessage(ChatColor.AQUA + "[RANDOMIZER] " + ChatColor.GREEN
                    + "The Random Pairs were successfully regenerated!");
            return true;
        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "Failed to save randomization configuration!");
            e.printStackTrace();
            return false;
        } 
    } 
}
