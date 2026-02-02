package me.funnyarmorbuffs.commands;

import me.funnyarmorbuffs.FunnyArmorBuffs;
import me.funnyarmorbuffs.util.ArmorHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorBuffCommand implements CommandExecutor, TabCompleter {

    private final FunnyArmorBuffs plugin;
    private final ArmorHelper armorHelper;

    public ArmorBuffCommand(FunnyArmorBuffs plugin, ArmorHelper armorHelper) {
        this.plugin = plugin;
        this.armorHelper = armorHelper;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("funnyarmorbuffs")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Использование: /funnyarmorbuffs <эффект> [уровень]");
                sender.sendMessage(ChatColor.RED + "Использование: /funnyarmorbuffs remove");
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                return handleRemoveCommand(sender, args);
            }

            String effectName = args[0].toUpperCase();
            String effectNameWithoutColor = ChatColor.stripColor(effectName);
            String englishEffectName = plugin.getRussianToEnglishEffects().getOrDefault(effectNameWithoutColor.toUpperCase(), effectName);
            PotionEffectType effectType = PotionEffectType.getByName(englishEffectName);

            if (effectType == null) {
                sender.sendMessage(ChatColor.RED + "Эффект '" + effectName + "' не найден!");
                return true;
            }

            if (sender instanceof Player player) {
                return handleApplyCommand(player, command, args, effectType);
            } else {
                return handleApplyCommandFromConsole(sender, command, args, effectType);
            }
        }
        return false;
    }

    private boolean handleApplyCommand(Player player, Command command, String[] args, PotionEffectType effectType) {
        if (!player.hasPermission("funnyarmorbuffs.apply")) {
            player.sendMessage(plugin.getPermissionMessage());
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            player.sendMessage(ChatColor.RED + "Использование: /funnyarmorbuffs <эффект> [уровень]");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !armorHelper.isArmor(item)) {
            player.sendMessage(ChatColor.RED + "Возьмите броню в руку!");
            return true;
        }

        int level = 1;
        if (args.length == 2) {
            try {
                level = Integer.parseInt(args[1]);
                if (level < 0) {
                    player.sendMessage(ChatColor.RED + "Уровень должен быть неотрицательным числом.");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Неверный формат числа. Используйте целое число для уровня.");
                return true;
            }
        }

        String translatedEffectName = plugin.getEffectTranslations().getOrDefault(effectType.getName(), effectType.getName());
        armorHelper.applyPotionEffectToArmor(item, effectType, level, player, plugin);
        player.sendMessage(ChatColor.GREEN + "Эффект '" + translatedEffectName + "' (уровень " + level + ") наложен на броню!");
        return true;
    }

    private boolean handleApplyCommandFromConsole(CommandSender sender, Command command, String[] args, PotionEffectType effectType) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Использование для консоли: /funnyarmorbuffs <эффект> <уровень> <игрок>");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
            if (level < 0) {
                sender.sendMessage(ChatColor.RED + "Уровень должен быть неотрицательным числом.");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Неверный формат числа. Используйте целое число для уровня.");
            return true;
        }

        String playerName = args[2];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Игрок '" + playerName + "' не найден!");
            return true;
        }

        PlayerInventory inventory = target.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        if (item == null || !armorHelper.isArmor(item)) {
            sender.sendMessage(ChatColor.RED + "У игрока '" + target.getName() + "' в руке нет брони!");
            return true;
        }

        armorHelper.applyPotionEffectToArmor(item, effectType, level, target, plugin);
        String translatedEffectName = plugin.getEffectTranslations().getOrDefault(effectType.getName(), effectType.getName());
        sender.sendMessage(ChatColor.GREEN + "Эффект '" + translatedEffectName + "' (уровень " + level + ") наложен на броню игрока " + target.getName() + "!");
        return true;
    }


    private boolean handleRemoveCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду можно выполнять только из консоли с указанием имени игрока");
            return false;
        }

        if (!player.hasPermission("funnyarmorbuffs.apply")) {
            sender.sendMessage(plugin.getPermissionMessage());
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !armorHelper.isArmor(item)) {
            player.sendMessage(ChatColor.RED + "Возьмите броню в руку!");
            return true;
        }

        armorHelper.removePotionEffectFromArmor(item, player, plugin);
        player.sendMessage(ChatColor.GREEN + "Эффект зелья удален с брони!");
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("funnyarmorbuffs")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>(List.of("remove"));
                String partialName = args[0].toLowerCase();
                List<String> effectNameCompletions = plugin.getPotionEffectNames().stream()
                        .filter(name -> name != null)
                        .map(name -> (String) name)
                        .filter(name -> name.toLowerCase().startsWith(partialName) || plugin.getEffectTranslations().values().stream().anyMatch(translation -> translation.toLowerCase().startsWith(partialName)))
                        .toList();
                completions.addAll(effectNameCompletions);
                return completions;
            }

            if (args.length == 2) {
                return Arrays.asList("1", "2", "3", "4", "5");
            }

            if (args.length == 3 && !(sender instanceof Player)) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .toList();
            }
        }
        return null;
    }
}