package pl.bkkuc.purutils.inventory;

import de.tr7zw.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pl.bkkuc.purutils.ColorUtility;
import pl.bkkuc.purutils.inventory.item.ItemBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryBuilder {

    public static Inventory fastBuild(InventoryHolder holder, int rows, String title){
        return Bukkit.createInventory(holder, rows <= 0 ? 9 : Math.min(rows, 6), PlaceholderAPI.setPlaceholders(null, ColorUtility.colorize(title)));
    }

    public static void buildSchem(Inventory inventory, ConfigurationSection section){
        if(section == null) return;

        Map<Character, ItemStack> itemMappings = new HashMap<>();

        List<String> schem = section.getStringList("schem");
        ConfigurationSection items = section.getConfigurationSection("items");

        if(items != null){
            items.getValues(false).forEach((k, v) -> {
                char symbol = k.charAt(0);
                ConfigurationSection itemConfig = (ConfigurationSection) v;
                if (itemConfig.get("material") != null) {
                    ItemStack item = ItemBuilder.fromConfiguration(items.getConfigurationSection(String.valueOf(symbol)));
                    NBTItem nbt = new NBTItem(item);
                    String type = items.getString(symbol + ".type", null);
                    nbt.setString(String.valueOf(symbol), type);
                    nbt.applyNBT(item);
                    itemMappings.put(symbol, item);
                }
            });
        }

        int index = 0;

        for (String row : schem) {
            for (String symbol : row.split(" ")) {
                char charSymbol = symbol.charAt(0);
                ItemStack item = itemMappings.getOrDefault(charSymbol, new ItemStack(Material.AIR));

                inventory.setItem(index++, item.clone());
            }
        }
    }
}
