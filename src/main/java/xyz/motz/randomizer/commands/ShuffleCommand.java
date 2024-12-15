package xyz.motz.randomizer.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import xyz.motz.randomizer.main.Randomizer;

import java.util.List;
import java.util.Random;

public class ShuffleCommand implements CommandExecutor {

    private List<?> blacklist;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Randomizer.getPlugin().getConfig();

        Randomizer.getPlugin().remaining.clear();

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
                        Randomizer.getPlugin().getConfig().set("partners." + mat, Randomizer.getPlugin().remaining.get(rand).toString());
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
                    Randomizer.getPlugin().getConfig().set("partners-mobs." + mob, Randomizer.getPlugin().remainingmobs.get(rand).toString());
                    Randomizer.getPlugin().remainingmobs.remove(rand);
                }
            }

        Randomizer.getPlugin().saveConfig();

        sender.sendMessage(ChatColor.AQUA + "[RANDOMIZER] " + ChatColor.GREEN
                + "The Random Pairs were successfully regenerated!");
        return true;
    }
}
