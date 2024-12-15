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

    public ShuffleCommand() {
        Randomizer plugin = Randomizer.getPlugin();
        itemsFile = new File(plugin.getDataFolder(), "items.yml");

        // Create items.yml if it doesn't exist
        if (!itemsFile.exists()) {
            try {
                itemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Randomizer.getPlugin().getConfig();
        Randomizer.getPlugin().remaining.clear();
        itemsConfig.set("blocks", null);
        itemsConfig.set("mobs", null);

        for (Material mat : Material.values()) {
            if (mat.isItem()) {
                blacklist = Randomizer.getPlugin().getConfig().getList("blacklist");
                if (!blacklist.contains(mat)) {
                    Randomizer.getPlugin().remaining.add(mat);
                }
            }
        }

        for (Material mat : Material.values()) {
            if (!Randomizer.getPlugin().remaining.isEmpty()) {

                    if (mat.isBlock()) {
                        Random r = new Random();
                        int rand;
                        if (Randomizer.getPlugin().remaining.size() != 1) {
                            rand = r.nextInt(Randomizer.getPlugin().remaining.size() - 1);
                        } else {
                            rand = 0;
                        }
                        itemsConfig.set("partners." + mat, Randomizer.getPlugin().remaining.get(rand).toString());
                        Randomizer.getPlugin().remaining.remove(rand);
                    }
                }
            }



        // TODO: SHUFFLE IT FOR MOBS
        Randomizer.getPlugin().remainingmobs.clear();

        for (Material mat : Material.values()) {
            blacklist = Randomizer.getPlugin().getConfig().getList("blacklist");
            if (!blacklist.contains(mat)) {
                if (mat.isItem()) {
                    Randomizer.getPlugin().remainingmobs.add(mat);
                }
            }
        }

        for (EntityType mob : EntityType.values()) {
            if (!Randomizer.getPlugin().remainingmobs.isEmpty()) {




                    Random r = new Random();
                    int rand;
                    if (Randomizer.getPlugin().remainingmobs.size() != 1) {
                        rand = r.nextInt(Randomizer.getPlugin().remainingmobs.size() - 1);
                    } else {
                        rand = 0;
                    }
                    itemsConfig.set("partners-mobs." + mob, Randomizer.getPlugin().remainingmobs.get(rand).toString());
                    Randomizer.getPlugin().remainingmobs.remove(rand);
                }
            }

        try {
            itemsConfig.save(itemsFile);
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Failed to save randomization!");
            return false;
        }

        sender.sendMessage(ChatColor.AQUA + "[RANDOMIZER] " + ChatColor.GREEN
                + "The Random Pairs were successfully regenerated!");
        return true;
    }
}
