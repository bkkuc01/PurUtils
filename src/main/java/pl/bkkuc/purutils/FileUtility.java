package pl.bkkuc.purutils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FileUtility {

    public static FileConfiguration get(JavaPlugin javaPlugin, String name){
        File file = new File(javaPlugin.getDataFolder(), name);
        if(javaPlugin.getResource(name) == null) return save(javaPlugin, YamlConfiguration.loadConfiguration(file), name);
        if(!file.exists()) javaPlugin.saveResource(name, false);
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration save(JavaPlugin javaPlugin, FileConfiguration configuration, String name){
        try {
            configuration.save(new File(javaPlugin.getDataFolder(), name));
        } catch (IOException e){
            e.printStackTrace();
        }
        return configuration;
    }
}
