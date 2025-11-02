package de.scholle.playersleep;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PlayerSleep extends JavaPlugin implements Listener, TabExecutor {

    private boolean enabled;
    private String mode;
    private int value;
    private final Set<Player> sleepingPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("playersleep")).setExecutor(this);
        Objects.requireNonNull(getCommand("playersleep")).setTabCompleter(this);
        getLogger().info("PlayerSleep enabled!");
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        enabled = config.getBoolean("enabled", true);
        mode = config.getString("mode", "percentage");
        value = config.getInt("value", 50);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!enabled || event.isCancelled()) return;
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getTime() < 12541 || world.getTime() > 23458) return;
        sleepingPlayers.add(player);
        int online = (int) world.getPlayers().stream().filter(p -> !p.isDead()).count();
        int required = calculateRequiredPlayers(online);
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Sleep] " + ChatColor.YELLOW +
                player.getName() + " went to bed (" + sleepingPlayers.size() + "/" + required + ")");
        if (sleepingPlayers.size() >= required) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "[Sleep] Enough players are sleeping! Skipping the night...");
            world.setTime(0);
            world.setStorm(false);
            world.setThundering(false);
            sleepingPlayers.clear();
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        sleepingPlayers.remove(event.getPlayer());
    }

    private int calculateRequiredPlayers(int online) {
        if (mode.equalsIgnoreCase("percentage")) {
            int percent = Math.max(0, Math.min(100, value));
            int required = (int) Math.ceil((percent / 100.0) * online);
            return Math.max(1, required);
        } else if (mode.equalsIgnoreCase("amount")) {
            return Math.min(value, online);
        }
        return online;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("playersleep.command")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "=== PlayerSleep Commands ===");
            sender.sendMessage(ChatColor.YELLOW + "/playersleep reload " + ChatColor.WHITE + "- Reloads the config");
            sender.sendMessage(ChatColor.YELLOW + "/playersleep set <enabled|mode|value> <newValue> " + ChatColor.WHITE + "- Edit config values");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            loadConfigValues();
            sender.sendMessage(ChatColor.GREEN + "[PlayerSleep] Config reloaded successfully!");
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /playersleep set <enabled|mode|value> <newValue>");
                return true;
            }

            String key = args[1].toLowerCase();
            String newValue = args[2];

            switch (key) {
                case "enabled":
                    boolean newEnabled = Boolean.parseBoolean(newValue);
                    getConfig().set("enabled", newEnabled);
                    break;
                case "mode":
                    if (!newValue.equalsIgnoreCase("percentage") && !newValue.equalsIgnoreCase("amount")) {
                        sender.sendMessage(ChatColor.RED + "Mode must be 'percentage' or 'amount'.");
                        return true;
                    }
                    getConfig().set("mode", newValue.toLowerCase());
                    break;
                case "value":
                    try {
                        int newVal = Integer.parseInt(newValue);
                        getConfig().set("value", newVal);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Value must be a number.");
                        return true;
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown setting: " + key);
                    return true;
            }

            saveConfig();
            loadConfigValues();
            sender.sendMessage(ChatColor.GREEN + "[PlayerSleep] Updated '" + key + "' to " + newValue);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Unknown command. Use /playersleep for help.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!sender.hasPermission("playersleep.command")) return Collections.emptyList();
        if (args.length == 1) {
            return Arrays.asList("reload", "set");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            return Arrays.asList("enabled", "mode", "value");
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            switch (args[1].toLowerCase()) {
                case "enabled":
                    return Arrays.asList("true", "false");
                case "mode":
                    return Arrays.asList("percentage", "amount");
                case "value":
                    return Arrays.asList("10", "25", "50", "75", "100");
            }
        }
        return Collections.emptyList();
    }
}
