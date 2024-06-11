package pl.bkkuc.purutils.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class FileImpl implements IFile {

    private final String name;
    private final @Nullable String folder;

    private final JavaPlugin javaPlugin;
    private FileConfiguration config;

    private final String path;

    public FileImpl(String name, @Nullable String folder, JavaPlugin javaPlugin) {
        this.name = name;
        this.folder = folder;
        this.javaPlugin = javaPlugin;
        this.path = javaPlugin.getDataFolder() + (folder == null ? "" : "/" + folder);
    }

    public FileImpl(String name, JavaPlugin javaPlugin) {
        this(name, null, javaPlugin);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable String getFolder() {
        return folder;
    }

    @Override
    public FileConfiguration getConfig() {
        java.io.File file = new java.io.File(path, name);
        if (javaPlugin.getResource(name) == null) return save();
        if (!file.exists()) javaPlugin.saveResource(name, false);
        config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    @Override
    public FileConfiguration save() {
        try {
            config.save(new File(path, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}