package pl.bkkuc.purutils.builders;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.purutils.builders.impl.EntityBuilder;
import pl.bkkuc.purutils.builders.impl.EquipmentBuilder;
import pl.bkkuc.purutils.builders.impl.ParticleBuilder;
import pl.bkkuc.purutils.inventory.item.ItemBuilder;

import java.util.List;
import java.util.Locale;

/**
 * Class for build object from configuration.
 */
public class BuilderManager {

    public static @Nullable ItemStack itemStack(ConfigurationSection section, @Nullable Player player) {
        if (section == null) return null;
        return ItemBuilder.fromConfiguration(section, player);
    }

    public static @Nullable ItemStack itemStack(ConfigurationSection section) {
        return itemStack(section, null);
    }

    public static @Nullable EquipmentBuilder equipmentBuilder(ConfigurationSection section) {
        if (section == null) return null;

        EquipmentBuilder equipmentBuilder = new EquipmentBuilder();

        for (String key : section.getKeys(false)) {
            String lowerKey = key.toLowerCase(Locale.ROOT);
            ConfigurationSection itemSection = section.getConfigurationSection(key);

            switch (lowerKey) {
                case "шляпа", "шлем", "голова", "head", "helmet" -> equipmentBuilder.helmet(itemStack(itemSection));
                case "нагрудник", "jacket", "жилет", "сиськи", "грудь", "chestplate" -> equipmentBuilder.chestplate(itemStack(itemSection));
                case "нога", "ноги", "legs", "leg", "legging", "ляжка", "ляжки", "leggings" -> equipmentBuilder.leggings(itemStack(itemSection));
                case "носки", "ботинки", "лапки", "feets", "feet", "boot", "boots" -> equipmentBuilder.boots(itemStack(itemSection));
                case "hand", "рука", "main-hand", "main_hand", "mainhand" -> equipmentBuilder.mainHand(itemStack(itemSection));
                case "2рука", "off-hand", "off_hand", "offhand" -> equipmentBuilder.offHand(itemStack(itemSection));
            }
        }

        return equipmentBuilder;
    }

    public static @Nullable Entity entity(ConfigurationSection section, @NotNull Location location) {
        if (section == null) return null;

        String entityTypeName = findEntityTypeName(section);
        if (entityTypeName == null) return null;

        EntityBuilder entityBuilder = new EntityBuilder(EntityType.valueOf(entityTypeName));
        entityBuilder.location(location);
        entityBuilder.equipmentBuilder(equipmentBuilder(section.getConfigurationSection("equipments")));
        entityBuilder.baby(section.getBoolean("baby", false));
        entityBuilder.glowing(section.getBoolean("glow", false));
        entityBuilder.customName(section.getString("name"));
        entityBuilder.maxHealth(section.getDouble("health"));

        entityBuilder.spawn();
        return entityBuilder.entity();
    }

    public static @Nullable ParticleBuilder particleBuilder(ConfigurationSection section) {
        return ParticleBuilder.fromConfiguration(section);
    }

    private static String findEntityTypeName(ConfigurationSection section) {
        List<String> similarity = List.of("entitytype", "entity-type", "type", "mob", "mobtype", "mob-type", "typ", "entitytyp", "entity-typ", "моб", "тип");
        return section.getKeys(false).stream()
                .filter(key -> similarity.contains(key) && section.get(key) instanceof String)
                .findFirst()
                .map(section::getString)
                .orElse(null);
    }
}