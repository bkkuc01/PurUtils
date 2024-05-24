package pl.bkkuc.purutils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InventoryPrevious implements Listener {

    private final List<User> previousInventories;

    private final JavaPlugin javaPlugin;

    public InventoryPrevious(JavaPlugin javaPlugin){
        this.javaPlugin = javaPlugin;
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);

        this.previousInventories = new CopyOnWriteArrayList<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onInventoryClose(InventoryCloseEvent e){
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getPlayer();
        User user = getUserByPlayer(player);

        if(e.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) {
            addInventoryToUser(user, player, e.getInventory());
            return;
        }

        if(user != null){
            previousInventories.remove(user);
        }
    }

    private void addInventoryToUser(User user, Player player, Inventory inventory) {
        if(user != null){
            user.addInventory(inventory);
        } else {
            user = new User(player);
            user.addInventory(inventory);
            previousInventories.add(user);
        }
    }

    public @Nullable User getUserByPlayer(Player player){
        return previousInventories.stream().filter(user -> user.getPlayer().getName().equals(player.getName())).findFirst().orElse(null);
    }

    public static class User {

        private final Player player;
        private final List<Inventory> inventories;

        public User(Player player){
            this.player = player;
            this.inventories = new ArrayList<>();
        }

        public void addInventory(Inventory inventory){
            inventories.add(inventory);
        }

        public void openPreviousInventory(){
            if(inventories.isEmpty()) return;
            int index = inventories.size() - 1;
            player.openInventory(inventories.get(index));
            inventories.remove(index);
        }

        public Player getPlayer() {
            return player;
        }

        public List<Inventory> getInventories() {
            return inventories;
        }
    }
}