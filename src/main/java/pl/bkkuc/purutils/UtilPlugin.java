package pl.bkkuc.purutils;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bkkuc.purutils.other.Updater;

public class UtilPlugin extends JavaPlugin {

    private static UtilPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("");
        getLogger().info(" Enabled plugin PurUtils version " + getDescription().getVersion());
        getLogger().info("           by developer bkkuc (t.me/bkkuc)");
        getLogger().info("");

        Updater.check(getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public static UtilPlugin getInstance() {
        return instance;
    }
}
