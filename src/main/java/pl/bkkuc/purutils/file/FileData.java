package pl.bkkuc.purutils.file;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface FileData {

    Map<String, Object> getDatas();

    /**
     * Load object to datas.
     * @param key Key
     * @param object Value
     */
    void loadToData(String key, Object object);

    /**
     * @param key Path in data
     * @return Object or null from datas.
     */
    @Nullable Object get(String key);

    /**
     * @param key Path in data
     * @return Value or null by key from datas.
     */
    @Nullable String getString(String key);

    /**
     * @param key Path in data
     * @return Value or def by key from datas.
     */
    @Nullable String getString(String key, String def);

    /**
     * @param key Path in data
     * @return List string or empty list collection by key from datas.
     */
    List<String> getStringList(String key);

    /**
     * @param key Path in data
     * @return Value int or {@code -1} by key from datas.
     */
    int getInt(String key);

    /**
     * @param key Path in data
     * @return Value int or def int by key from datas.
     */
    int getInt(String key, int def);

    /**
     * @param key Path in data
     * @return Value double or {@code -1.0} by key from datas.
     */
    double getDouble(String key);

    /**
     * @param key Path in data
     * @return Value double or def by key from datas.
     */
    double getDouble(String key, double def);

    /**
     * @param key Path in data
     * @return Configuration section or null from datas.
     */
    @Nullable ConfigurationSection getConfigurationSection(String key);
}
