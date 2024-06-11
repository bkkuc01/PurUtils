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

import java.util.Arrays;
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
                case "носки":
                case "ботинки":
                case "лапки":
                case "feets":
                case "feet":
                case "boot":
                case "boots": {
                    equipmentBuilder.boots(itemStack(itemSection));
                    break;
                }
                case "hand":
                case "рука":
                case "main-hand":
                case "main_hand":
                case "mainhand": {
                    equipmentBuilder.mainHand(itemStack(itemSection));
                    break;
                }
                case "2рука":
                case "off-hand":
                case "off_hand":
                case "offhand": {
                    equipmentBuilder.offHand(itemStack(itemSection));
                    break;
                }
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
        List<String> similarity = Arrays.asList("entitytype", "entity-type", "type", "mob", "mobtype", "mob-type", "typ", "entitytyp", "entity-typ", "моб", "тип");
        return section.getKeys(false).stream()
                .filter(key -> similarity.contains(key) && section.get(key) instanceof String)
                .findFirst()
                .map(section::getString)
                .orElse(null);
    }
}