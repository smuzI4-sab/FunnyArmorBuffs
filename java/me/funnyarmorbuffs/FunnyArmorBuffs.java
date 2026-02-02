package me.funnyarmorbuffs;

import me.funnyarmorbuffs.commands.ArmorBuffCommand;
import me.funnyarmorbuffs.listeners.ArmorBuffListener;
import me.funnyarmorbuffs.util.ArmorHelper;
import me.funnyarmorbuffs.util.ConfigManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class FunnyArmorBuffs extends JavaPlugin {

    private String permissionMessage;
    private NamespacedKey potionEffectKey;
    private NamespacedKey potionLevelKey;
    private boolean displayEffectInLore;
    private ArmorBuffListener armorBuffListener;
    public ArmorBuffCommand armorBuffCommand;
    private ConfigManager configManager;
    public ArmorHelper armorHelper;

    @Override
    public void onEnable() {
        getLogger().info("FunnyArmorBuffs включен!");

        armorHelper = new ArmorHelper();
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        configManager.loadEffectTranslations();

        permissionMessage = configManager.getPermissionMessage();
        displayEffectInLore = configManager.isDisplayEffectInLore();

        potionEffectKey = new NamespacedKey(this, "potion_effect");
        potionLevelKey = new NamespacedKey(this, "potion_level");

        armorBuffListener = new ArmorBuffListener(this, armorHelper, potionEffectKey, potionLevelKey, displayEffectInLore, configManager.getEffectTranslations());
        getServer().getPluginManager().registerEvents(armorBuffListener, this);

        armorBuffCommand = new ArmorBuffCommand(this, armorHelper);
        getCommand("funnyarmorbuffs").setExecutor(armorBuffCommand);
        getCommand("funnyarmorbuffs").setTabCompleter(armorBuffCommand);
    }

    @Override
    public void onDisable() {
        getLogger().info("FunnyArmorBuffs выключен!");
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public ArmorBuffListener getArmorBuffListener() {
        return armorBuffListener;
    }

    public NamespacedKey getPotionEffectKey() {
        return potionEffectKey;
    }

    public NamespacedKey getPotionLevelKey() {
        return potionLevelKey;
    }

    public boolean isDisplayEffectInLore() {
        return displayEffectInLore;
    }

    public java.util.Map<String, String> getEffectTranslations() {
        return configManager.getEffectTranslations();
    }

    public java.util.Map<String, String> getRussianToEnglishEffects() {
        return configManager.getRussianToEnglishEffects();
    }

    public java.util.List<String> getPotionEffectNames() {
        return configManager.getPotionEffectNames();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}