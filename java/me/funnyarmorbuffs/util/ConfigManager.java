package me.funnyarmorbuffs.util;

import me.funnyarmorbuffs.FunnyArmorBuffs;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigManager {

    private final FunnyArmorBuffs plugin;
    private String permissionMessage;
    private boolean displayEffectInLore;
    public Map<String, String> effectTranslations = new HashMap<>();
    public Map<String, String> russianToEnglishEffects = new HashMap<>();
    private List<String> potionEffectNames = new ArrayList<>();
    private String loreFormat;
    private String effectColor;
    private String levelColor;

    public ConfigManager(FunnyArmorBuffs plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        permissionMessage = ChatColor.translateAlternateColorCodes('&', config.getString("permission_message", "&cУ вас нет прав!"));
        displayEffectInLore = config.getBoolean("display_effect_in_lore", true);

        loreFormat = ChatColor.translateAlternateColorCodes('&', config.getString("lore_format", "&7Эффект: &r%effect% %level%"));
        effectColor = ChatColor.translateAlternateColorCodes('&', config.getString("effect_color", "&b"));
        levelColor = ChatColor.translateAlternateColorCodes('&', config.getString("level_color", "&6"));
    }

    public void loadEffectTranslations() {
        FileConfiguration config = plugin.getConfig();
        config.addDefault("effect_translations.SPEED", "&bСкорость");
        config.addDefault("effect_translations.SLOW", "&7Замедление");
        config.addDefault("effect_translations.INCREASE_DAMAGE", "&cСила");
        config.addDefault("effect_translations.HEAL", "&dМгновенное лечение");
        config.addDefault("effect_translations.HARM", "&4Мгновенный урон");
        config.addDefault("effect_translations.JUMP", "&aПрыгучесть");
        config.addDefault("effect_translations.REGENERATION", "&eРегенерация");
        config.addDefault("effect_translations.DAMAGE_RESISTANCE", "&9Сопротивление урону");
        config.addDefault("effect_translations.FIRE_RESISTANCE", "&6Огнестойкость");
        config.addDefault("effect_translations.WATER_BREATHING", "&3Подводное дыхание");
        config.addDefault("effect_translations.INVISIBILITY", "&8Невидимость");
        config.addDefault("effect_translations.BLINDNESS", "&0Слепота");
        config.addDefault("effect_translations.NIGHT_VISION", "&fНочное зрение");
        config.addDefault("effect_translations.HUNGER", "&2Голод");
        config.addDefault("effect_translations.WEAKNESS", "&8Слабость");
        config.addDefault("effect_translations.POISON", "&9Отравление");
        config.addDefault("effect_translations.WITHER", "&0Иссушение");
        config.addDefault("effect_translations.HEALTH_BOOST", "&cУвеличение здоровья");
        config.addDefault("effect_translations.ABSORPTION", "&eПоглощение");
        config.addDefault("effect_translations.SATURATION", "&fНасыщение");
        config.addDefault("effect_translations.GLOWING", "&eСвечение");
        config.addDefault("effect_translations.LEVITATION", "&5Левитация");
        config.addDefault("effect_translations.LUCK", "&bУдача");
        config.addDefault("effect_translations.UNLUCK", "&4Неудача");
        config.addDefault("effect_translations.SLOW_FALLING", "&fМедленное падение");
        config.addDefault("effect_translations.CONDUIT_POWER", "&3Сила проводника");
        config.addDefault("effect_translations.DOLPHINS_GRACE", "&3Грация дельфина");
        config.addDefault("effect_translations.BAD_OMEN", "&2Дурное предзнаменование");
        config.addDefault("effect_translations.HERO_OF_THE_VILLAGE", "&dГерой деревни");
        config.addDefault("effect_translations.FAST_DIGGING", "&6Спешка");
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

        for (String key : config.getConfigurationSection("effect_translations").getKeys(false)) {
            String effectName = key.toUpperCase();
            String translation = config.getString("effect_translations." + key);
            String formattedTranslation = ChatColor.translateAlternateColorCodes('&', translation);
            effectTranslations.put(effectName, formattedTranslation);
            russianToEnglishEffects.put(translation.toUpperCase(), effectName);
        }

        potionEffectNames = Arrays.stream(PotionEffectType.values()).map(PotionEffectType::getName).collect(Collectors.toList());
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public boolean isDisplayEffectInLore() {
        return displayEffectInLore;
    }

    public Map<String, String> getEffectTranslations() {
        return effectTranslations;
    }

    public Map<String, String> getRussianToEnglishEffects() {
        return russianToEnglishEffects;
    }

    public List<String> getPotionEffectNames() {
        return potionEffectNames;
    }

    public String getLoreFormat() {
        return loreFormat;
    }

    public String getEffectColor() {
        return effectColor;
    }

    public String getLevelColor() {
        return levelColor;
    }
}