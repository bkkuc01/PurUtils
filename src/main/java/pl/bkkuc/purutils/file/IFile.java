package pl.bkkuc.purutils.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

public interface IFile {

    String getName();

    /**
     * @return Возвращает папку которая в папке JavaPlugin.
     */
    @Nullable String getFolder();

    FileConfiguration getConfig();

    FileConfiguration save();
}