package pl.bkkuc.purutils;

import com.destroystokyo.paper.ParticleBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Utility {

    public static void doActions(@Nullable Player player, List<String> actions, boolean papi){
        if(actions == null || actions.isEmpty()) return;

        for(String action: actions){
            if(player != null) action = action.replace("{player}", player.getName());
            if(papi) action = PlaceholderAPI.setPlaceholders(player, action);
            String[] params = action.split(" ");
            switch (params[0].toLowerCase()){
                case "[message]": {
                    if(player != null){
                        player.sendMessage(ColorUtility.colorize(ATS(params, 1)));
                    }
                    break;
                }
                case "[title]": {
                    if(player != null){
                        String[] title = ColorUtility.colorize(ATS(params, 1)).split(";");
                        player.sendTitle(title[0], title.length >= 2 ? title[1] : "");
                    }

                    break;
                }
                case "[player]": {
                    if(player != null){
                        player.chat(ATS(params, 1));
                    }
                    break;
                }
                case "[broadcast]": {

                    for(Player online: Bukkit.getOnlinePlayers()){
                        String message = ColorUtility.colorize(ATS(params, 1));
                        online.sendMessage(message);
                    }

                    break;
                }
                case "[broadcast-sound]": {

                    for(Player online: Bukkit.getOnlinePlayers()) {
                        Sound sound;

                        try {
                            sound = Sound.valueOf(params[1].toUpperCase());
                        } catch (IllegalArgumentException ignored){
                            break;
                        }

                        float volume = params.length >= 3 ? Float.parseFloat(params[2]) : 1.0f;
                        float pitch = params.length >= 4 ? Float.parseFloat(params[3]) : 1.0f;

                        online.playSound(online.getLocation(), sound, volume, pitch);
                    }

                    break;
                }
                case "[broadcast-title]": {

                    for(Player online: Bukkit.getOnlinePlayers()) {
                        String[] title = ColorUtility.colorize(ATS(params, 1)).split(";");
                        online.sendTitle(title[0], title.length >= 2 ? title[1] : "");
                    }

                    break;
                }
                case "[sound]": {
                    if(player == null) break;

                    Sound sound;

                    try {
                        sound = Sound.valueOf(params[1].toUpperCase());
                    } catch (IllegalArgumentException ignored){
                        break;
                    }

                    float volume = params.length >= 3 ? Float.parseFloat(params[2]) : 1.0f;
                    float pitch = params.length >= 4 ? Float.parseFloat(params[3]) : 1.0f;

                    player.playSound(player.getLocation(), sound, volume, pitch);
                    break;
                }
                case "[console]": {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ATS(params, 1));
                    break;
                }
                case "[particle]": {
                    if(player == null) return;

                    Particle particle;
                    try {
                        particle = Particle.valueOf(params[0].toUpperCase());
                    } catch (IllegalArgumentException ignored){
                        break;
                    }

                    int count = params.length >= 3 ? Integer.parseInt(params[2]) : 5;
                    float extra = params.length >= 4 ? Float.parseFloat(params[3]) : 1.0f;

                    new ParticleBuilder(particle).count(count).extra(extra).location(player.getLocation()).spawn();
                    break;
                }
                case "[actionbar]": {
                    if(player == null) break;
                    player.sendActionBar(ColorUtility.colorize(ATS(params, 1)));
                    break;
                }
            }
        }
    }

    public static String ATS(String[] array, int index){
        return String.join(" ", Arrays.copyOfRange(array, index, array.length));
    }
}
