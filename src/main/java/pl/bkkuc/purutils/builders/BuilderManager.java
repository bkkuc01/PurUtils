package pl.bkkuc.purutils.builders;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.purutils.builders.impl.EntityBuilder;
import pl.bkkuc.purutils.builders.impl.EquipmentBuilder;
import pl.bkkuc.purutils.builders.impl.ParticleBuilder;
import pl.bkkuc.purutils.inventory.item.ItemBuilder;

import java.util.ArrayList;
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
        if(section == null) return null;
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

    public static @Nullable Entity entity(@NotNull ConfigurationSection section, @NotNull Location location) {
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

    public static @Nullable ParticleBuilder particleBuilder(@NotNull ConfigurationSection section) {
        return ParticleBuilder.fromConfiguration(section);
    }

    /**
     * Example in configuration section:
     * inventory:
     *   ...
     *   items:
     *     4:
     *       name: ...
     *       lore: ...
     *       ...
     * @return Successfully
     */
    public boolean inventoryBuilderBySlot(@NotNull Inventory inventory, @NotNull ConfigurationSection section, @Nullable Player player) {

        for(String maybeSlot: section.getKeys(false)) {
            int slot;
            try {
                slot = Integer.parseInt(maybeSlot);
            } catch (NumberFormatException e){ continue; }

            if(slot < 0 || slot > inventory.getSize()) continue;

            inventory.setItem(slot, itemStack(section.getConfigurationSection(maybeSlot), player));
        }

        return true;
    }

    /**
     * Example in configuration section:
     * inventory:
     *   ...
     *   items:
     *     'nameable':
     *       slot: 45
     *       name: ...
     *       lore: ...
     *       ...
     *     'slots':
     *       slot:
     *         - 32
     *         - 31
     *       name: ...
     *       lore: ...
     *       ...
     * @return Successfully
     */
    public boolean inventoryBuilder(@NotNull Inventory inventory, @NotNull ConfigurationSection section, @Nullable Player player) {

        for(String iconName: section.getKeys(false)) {
            ConfigurationSection iconSection = section.getConfigurationSection(iconName);
            if(iconSection == null) continue;

            Object slotObject = iconSection.get("slot");
            if(slotObject == null) continue;

            List<Integer> slots = new ArrayList<>();
            if(slotObject instanceof Integer) {
                slots.add((Integer) slotObject);
            }
            else if(slotObject instanceof List<?>) {
                List<?> l = (List<?>) slotObject;

                for(Object o: l) {
                    if(o instanceof Integer){
                        slots.add((Integer) o);
                    }
                }
            }

            for(Integer slot: slots){
                if(slot > 0 && slot < inventory.getSize()) {
                    inventory.setItem(slot, itemStack(section.getConfigurationSection(iconName), player));
                }
            }
        }

        return true;
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