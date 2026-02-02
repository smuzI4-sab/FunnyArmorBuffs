package me.funnyarmorbuffs.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.funnyarmorbuffs.FunnyArmorBuffs;
import me.funnyarmorbuffs.util.ArmorHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class ArmorBuffListener implements Listener {

    public final FunnyArmorBuffs plugin;
    public final ArmorHelper armorHelper;
    private final NamespacedKey potionEffectKey;
    private final NamespacedKey potionLevelKey;
    public final boolean displayEffectInLore;
    public final Map<String, String> effectTranslations;

    public ArmorBuffListener(FunnyArmorBuffs plugin, ArmorHelper armorHelper, NamespacedKey potionEffectKey, NamespacedKey potionLevelKey, boolean displayEffectInLore, Map<String, String> effectTranslations) {
        this.plugin = plugin;
        this.armorHelper = armorHelper;
        this.potionEffectKey = potionEffectKey;
        this.potionLevelKey = potionLevelKey;
        this.displayEffectInLore = displayEffectInLore;
        this.effectTranslations = effectTranslations;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (ItemStack armorItem : player.getInventory().getArmorContents()) {
            applyArmorEffect(player, armorItem);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack oldItem = event.getOldItem();
        ItemStack newItem = event.getNewItem();

        removeArmorEffect(player, oldItem);
        applyArmorEffect(player, newItem);
    }

    private void removeArmorEffect(Player player, ItemStack armorItem) {
        if (armorItem != null && armorItem.getType() != Material.AIR) {
            ItemMeta meta = armorItem.getItemMeta();
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (container.has(potionEffectKey, PersistentDataType.STRING)) {
                    String effectName = container.get(potionEffectKey, PersistentDataType.STRING);
                    PotionEffectType effectType = PotionEffectType.getByName(effectName);
                    if (effectType != null) {
                        player.removePotionEffect(effectType);
                    }
                }
            }
        }
    }

    private void applyArmorEffect(Player player, ItemStack armorItem) {
        if (armorItem != null && armorItem.getType() != Material.AIR) {
            ItemMeta meta = armorItem.getItemMeta();
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (container.has(potionEffectKey, PersistentDataType.STRING)) {
                    String effectName = container.get(potionEffectKey, PersistentDataType.STRING);
                    int level = container.has(potionLevelKey, PersistentDataType.INTEGER) ? container.get(potionLevelKey, PersistentDataType.INTEGER) : 1;
                    PotionEffectType effectType = PotionEffectType.getByName(effectName);
                    if (effectType != null) {
                        player.addPotionEffect(new PotionEffect(effectType, Integer.MAX_VALUE, level - 1, false, false, true));
                    }
                }
            }
        }
    }
}