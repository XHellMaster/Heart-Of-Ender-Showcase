package net.heartofender.heartofender.player;

import net.heartofender.heartofender.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CustomPlayerManager implements Listener {
    private final Main plugin;
    public HashMap<UUID, CustomPlayer> players = new HashMap<>();

    public CustomPlayerManager() {
        this.plugin = Main.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        startRegenTask();
    }

    private void startRegenTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    // try getting from map
                    CustomPlayer customPlayer = players.get(player.getUniqueId());

                    // if not in map, create
                    // this case should be impossible because of the onPlayerJoin, but this is good practice
                    if (customPlayer == null) {
                        customPlayer = new CustomPlayer(player);
                        players.put(player.getUniqueId(), customPlayer);
                    }

                    // regen mana for each player
                    customPlayer.regen();
                }
            }
        }.runTaskTimer(plugin, 0, 20); // every second (20 ticks if server is nominal)
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Add player to hashmap
        Player player = event.getPlayer();
        CustomPlayer customPlayer = new CustomPlayer(player);
        players.put(player.getUniqueId(), customPlayer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = players.remove(player.getUniqueId());

        if (customPlayer != null) {
            customPlayer.saveConfig();
        }
    }

}
