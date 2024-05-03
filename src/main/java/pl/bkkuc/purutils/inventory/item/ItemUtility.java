package pl.bkkuc.purutils.inventory.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemUtility {

    /**
     * Check item to nullable or material air.
     */
    public static boolean isValid(@Nullable ItemStack item){
        return item != null && item.getType() != Material.AIR;
    }

    public static boolean isOre(ItemStack item){
        return item != null && item.getType().name().endsWith("_ORE");
    }

    public static boolean isBreakableBlock(Block block){
        if(block == null) return false;
        Material material = block.getType();
        switch (material){
            case BARRIER:
            case END_PORTAL_FRAME:
            case END_PORTAL:
            case NETHER_PORTAL:
            case BEDROCK:
            case COMMAND_BLOCK:
                return false;
            default:
                return true;
        }
    }
}
