package pl.bkkuc.purutils.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

public interface IFile {

    void init();

    String getName();

    /**
     * @return Возвращает папку которая в папке JavaPlugin.
     */
    @Nullable String getFolder();

    FileConfiguration getConfig();
    FileData getData();

    FileConfiguration save();
}