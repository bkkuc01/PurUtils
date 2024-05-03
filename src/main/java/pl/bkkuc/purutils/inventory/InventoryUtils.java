package pl.bkkuc.purutils.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class InventoryUtils {

    /**
     * Get random slot from inventory.
     */
    public static int getRandomSlot(@NotNull Inventory inventory){
        return ThreadLocalRandom.current().nextInt(inventory.getSize());
    }

    /**
     * Get random empty slot from inventory
     */
    public static int getRandomEmptySlot(@NotNull Inventory inventory){
        List<Integer> emptySlots = new ArrayList<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if(item == null || item.getType() == Material.AIR) {
                emptySlots.add(i);
            }
        }
        if(emptySlots.isEmpty()) return -1;

        return ThreadLocalRandom.current().nextInt(emptySlots.size());
    }

    /**
     * Get empty slots from inventory
     */
    public static List<Integer> getEmptySlots(@NotNull Inventory inventory){
        List<Integer> emptySlots = new ArrayList<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if(item == null || item.getType() == Material.AIR) {
                emptySlots.add(i);
            }
        }
        return emptySlots;
    }

}
