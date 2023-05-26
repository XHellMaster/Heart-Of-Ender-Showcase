package net.heartofender.heartofender.features.magic.spells;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class SpellListener implements Listener {

    @EventHandler
    void onChangeBlock(EntityChangeBlockEvent e) {
        //Checking if it is a falling block
        if (e.getEntity() instanceof FallingBlock) {
            FallingBlock FB = (FallingBlock) e.getEntity();
            if (FB.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "tsunami"))) {
                if (FB.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "tsunami");
            NamespacedKey key1 = new NamespacedKey(Main.getInstance(), "spawner");
            NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "damage");

            if (fallingBlock.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                String id = fallingBlock.getPersistentDataContainer().get(key1, PersistentDataType.STRING);
                double damage = fallingBlock.getPersistentDataContainer().get(key2, PersistentDataType.DOUBLE);
                Player p = Bukkit.getPlayer(UUID.fromString(id));
                for (Entity entity : fallingBlock.getNearbyEntities(1, 1, 1)) {
                    if (entity instanceof LivingEntity le && !entity.equals(p)) {
                        if (entity instanceof Player p1) {
                            CustomPlayer target = new CustomPlayer(p1);
                            target.applyDamage(damage);
                        }
                        else ((LivingEntity) entity).damage(damage);
                    }
                }
            }
        }
    }
}
