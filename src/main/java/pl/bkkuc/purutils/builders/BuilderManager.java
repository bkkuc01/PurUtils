package pl.bkkuc.purutils.builders;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.purutils.builders.impl.EntityBuilder;
import pl.bkkuc.purutils.builders.impl.EquipmentBuilder;
import pl.bkkuc.purutils.builders.impl.ParticleBuilder;
import pl.bkkuc.purutils.database.DataBase;
import pl.bkkuc.purutils.database.DataBaseType;
import pl.bkkuc.purutils.database.databases.*;
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
    public static boolean inventoryBuilderBySlot(@NotNull Inventory inventory, @NotNull ConfigurationSection section, @Nullable Player player) {

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
    public static boolean inventoryBuilder(@NotNull Inventory inventory, @NotNull ConfigurationSection section, @Nullable Player player) {

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

    public static @Nullable DataBase dataBaseBuilder(@Nullable ConfigurationSection section, @NotNull JavaPlugin javaPlugin) {

        String dataBaseTypeName = section != null ? section.getString("type", "SQLITE") : "SQLITE";
        DataBaseType dataBaseType;
        try {
            dataBaseType = DataBaseType.valueOf(dataBaseTypeName.toUpperCase());
        } catch (IllegalArgumentException e){
            Bukkit.getLogger().severe("DataBase type '" + dataBaseTypeName + "' is not found. I will use SqLite");
            dataBaseType = DataBaseType.SQLITE;
        }

        String host         = "localhost";
        String port         = "3306";
        String username     = "root";
        String password     = "admin";
        String dataBaseName = javaPlugin.getDescription().getName().toLowerCase() + "_";

        if(section != null) {
            host         = section.getString("host", "localhost");
            port         = section.getString("port", "3306");
            username     = section.getString("username", "root");
            password     = section.getString("password", "admin");
            dataBaseName = section.getString("data-base-name", javaPlugin.getDescription().getName().toLowerCase() + "_");
        }

        DataBase dataBase = null;
        switch (dataBaseType) {
            case MYSQL: {
                dataBase = new MySQL(host, port, username, password, dataBaseName);
                break;
            }
            case SQLITE: {
                dataBase = new SqLite(javaPlugin, null);
                break;
            }
            case POSTGRESQL: {
                dataBase = new PostgreSQL(host, port, username, password, dataBaseName);
                break;
            }
            case MARIADB: {
                dataBase = new MariaDB(host, port, username, password, dataBaseName);
                break;
            }
        }

        return dataBase;
    }

}