package pl.bkkuc.purutils.inventory.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.purutils.ColorUtility;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {

    public static ItemStack buildItem(@Nullable Player player, String material, String name, List<String> lore){
        ItemStack item = new ItemStack(getMaterial(material, player != null ? player.getName() : null));
        ItemMeta meta = item.getItemMeta();

        name = name != null ? name.replace("{player}", player != null ? player.getName() : "{player}") : null;
        lore = lore != null && !lore.isEmpty() ? lore.stream().map(s -> s.replace("{player}", player != null ? player.getName() : "{player}")).collect(Collectors.toList()) : lore;

        if(name != null) meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, ColorUtility.colorize(name)));
        if(lore != null && !lore.isEmpty()) meta.setLore(PlaceholderAPI.setPlaceholders(player, lore.stream().map(ColorUtility::colorize).collect(Collectors.toList())));

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack buildItem(@Nullable Player player, String material, String name){
        return buildItem(player, material, name, null);
    }

    public static ItemStack fromConfiguration(ConfigurationSection section, @Nullable Player player){
        ItemStack item = buildItem(player, section.getString("material", "STONE"), section.getString("name"), section.getStringList("lore"));
        ItemMeta meta = item.getItemMeta();

        if(section.get("hide-flags") != null && !section.getStringList("hide-flags").isEmpty()){
            section.getStringList("hide-flags").forEach(flag -> {
                ItemFlag itemFlag;
                try {
                    itemFlag = ItemFlag.valueOf(flag);
                    meta.addItemFlags(itemFlag);
                } catch (IllegalArgumentException ignored){}
            });
        }
        if(section.get("enchantments") != null && !section.getStringList("enchantments").isEmpty()){
            section.getStringList("enchantments").forEach(ench -> {
                String[] params = ench.split(";");
                Enchantment enchantment;
                try {
                    enchantment = Enchantment.getByName(params[0]);
                    meta.addEnchant(enchantment, Integer.parseInt(params[1]), true);
                } catch (IllegalArgumentException ignored){}
            });
        }

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getMaterial(String material, @Nullable String playerName) {
        if (material.startsWith("head-"))
            return playerHead(material, playerName);
        if (material.startsWith("basehead-"))
            return getBase64Head(material.split("-")[1]);

        Material matchedMaterial = Material.matchMaterial(material);
        if (matchedMaterial == null) {
            System.out.printf("[PurUtils] Material '%s' is not found.", material);
            return new ItemStack(Material.STONE);
        }

        return new ItemStack(matchedMaterial, 1);
    }

    public static ItemStack playerHead(String owner, @Nullable String playerName) {
        ItemStack item;
        if (Material.matchMaterial("PLAYER_HEAD") != null) {
            item = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            item = new ItemStack(Material.valueOf("SKULL_ITEM"), 1);
            item.setDurability((short) 3);
        }
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner.split("-")[1].replace("{player}", playerName == null ? "{player}" : playerName));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getBase64Head(String value) {
        ItemStack item;
        if (Material.matchMaterial("PLAYER_HEAD") != null) {
            item = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            item = new ItemStack(Material.valueOf("SKULL_ITEM"), 1);
            item.setDurability((short) 3);
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        item.setItemMeta(meta);
        return item;
    }
}
