package pl.bkkuc.purutils.file;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDataImpl implements FileData {
    private final Map<String, Object> datas;

    public FileDataImpl(@NotNull FileConfiguration configuration){
        this.datas = new HashMap<>();

        configuration.getKeys(false).forEach(key -> {
            datas.put(key, configuration.get(key));
        });
    }

    @Override
    public Map<String, Object> getDatas() {
        return datas;
    }

    @Override
    public void loadToData(String key, Object object) {
        if(datas.containsKey(key)) return;

        datas.put(key, object);
    }

    @Override
    public @Nullable Object get(String key) {
        return datas.getOrDefault(key, null);
    }

    @Override
    public @Nullable String getString(String key) {
        Object o = get(key);
        if(o instanceof String) {
            return (String) o;
        }
        return null;
    }

    @Override
    public @Nullable String getString(String key, String def) {
        Object o = get(key);
        if(o instanceof String) {
            return (String) o;
        }

        return def;
    }

    @Override
    public List<String> getStringList(String key) {
        Object o = get(key);
        if(o instanceof List<?>) {
            return (List<String>) o;
        }

        return new ArrayList<>();
    }

    @Override
    public int getInt(String key) {
        Object o = get(key);
        if(o instanceof Integer) {
            return (int) o;
        }

        return -1;
    }

    @Override
    public int getInt(String key, int def) {
        Object o = get(key);
        if(o instanceof Integer) {
            return (int) o;
        }

        return def;
    }

    @Override
    public double getDouble(String key) {
        Object o = get(key);
        if(o instanceof Double) {
            return (double) o;
        }

        return -1;
    }

    @Override
    public double getDouble(String key, double def) {
        Object o = get(key);
        if(o instanceof Double) {
            return (double) o;
        }

        return def;
    }

    @Override
    public @Nullable ConfigurationSection getConfigurationSection(String key) {
        Object o = get(key);
        if(o instanceof ConfigurationSection) {
            return (ConfigurationSection) o;
        }

        return null;
    }
}
