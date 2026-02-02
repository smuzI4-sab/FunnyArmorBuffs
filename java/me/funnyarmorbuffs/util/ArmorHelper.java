package me.funnyarmorbuffs.util;

import me.funnyarmorbuffs.FunnyArmorBuffs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ArmorHelper {

    public boolean isArmor(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            Material type = item.getType();
            return type == Material.LEATHER_HELMET || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_LEGGINGS || type == Material.LEATHER_BOOTS || type == Material.IRON_HELMET || type == Material.IRON_CHESTPLATE || type == Material.IRON_LEGGINGS || type == Material.IRON_BOOTS || type == Material.GOLDEN_HELMET || type == Material.GOLDEN_CHESTPLATE || type == Material.GOLDEN_LEGGINGS || type == Material.GOLDEN_BOOTS || type == Material.CHAINMAIL_HELMET || type == Material.CHAINMAIL_CHESTPLATE || type == Material.CHAINMAIL_LEGGINGS || type == Material.CHAINMAIL_BOOTS || type == Material.DIAMOND_HELMET || type == Material.DIAMOND_CHESTPLATE || type == Material.DIAMOND_LEGGINGS || type == Material.DIAMOND_BOOTS || type == Material.NETHERITE_HELMET || type == Material.NETHERITE_CHESTPLATE || type == Material.NETHERITE_LEGGINGS || type == Material.NETHERITE_BOOTS;
        } else {
            return false;
        }
    }

    public void applyPotionEffectToArmor(ItemStack item, PotionEffectType effectType, int level, Player player, FunnyArmorBuffs plugin) {
        if (item != null && effectType != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                String effectName = effectType.getName();
                container.set(plugin.getPotionEffectKey(), PersistentDataType.STRING, effectName);
                container.set(plugin.getPotionLevelKey(), PersistentDataType.INTEGER, level);
                if (plugin.isDisplayEffectInLore()) {
                    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                    lore.removeIf(line -> line.startsWith(ChatColor.GRAY + "Эффект: "));

                    String translatedEffect = plugin.getEffectTranslations().getOrDefault(effectName, effectName);
                    String formattedLore = plugin.getConfigManager().getLoreFormat()
                            .replace("%effect%", plugin.getConfigManager().getEffectColor() + translatedEffect)
                            .replace("%level%", plugin.getConfigManager().getLevelColor() + level);

                    lore.add(formattedLore);
                    meta.setLore(lore);
                }

                item.setItemMeta(meta);
            }
        }
    }

    public void removePotionEffectFromArmor(ItemStack item, Player player, FunnyArmorBuffs plugin) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                container.remove(plugin.getPotionEffectKey());
                container.remove(plugin.getPotionLevelKey());
                if (plugin.isDisplayEffectInLore() && meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    lore.removeIf(line -> line.startsWith(ChatColor.GRAY + "Эффект: "));
                    meta.setLore(lore);
                }

                item.setItemMeta(meta);
            }
        }
    }
}