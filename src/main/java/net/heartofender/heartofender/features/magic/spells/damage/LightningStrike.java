package net.heartofender.heartofender.features.magic.spells.damage;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class LightningStrike extends Spell {

    public LightningStrike() {
        super(Element.AIR, Rarity.GRAND, 30, "Lightning Strike", "Calls down a bolt of lightning on an enemy, dealing high damage.", SClassifier.DAMAGE, "net.heartofender.heartofender.features.magic.spells.damage.LightningStrike", 21);
    }

    public LightningStrike(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        Location center = player.getLocation();

        // Create the Lightning Strike effect
        new BukkitRunnable() {
            int ticks = 0;
            int duration = 1;
            int maxDistance = 45;

            public void run() {
                ticks++;

                // Call down a bolt of lightning on the nearest enemy within range
                Entity nearestEnemy = null;
                double nearestDistance = Double.MAX_VALUE;
                for (Entity entity : center.getWorld().getNearbyEntities(center, maxDistance, maxDistance, maxDistance)) {
                    if (entity instanceof Player && entity != player) {
                        double distance = entity.getLocation().distance(center);
                        if (distance < nearestDistance) {
                            nearestEnemy = entity;
                            nearestDistance = distance;
                        }
                    }
                }
                if (nearestEnemy != null) {
                    nearestEnemy.getWorld().strikeLightning(nearestEnemy.getLocation());
                    CustomPlayer customPlayer = new CustomPlayer((Player) nearestEnemy);
                    customPlayer.applyDamage(10); // High damage
                }

                if (ticks >= duration * 20) { // Stop after the specified duration (in seconds)
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Adjust the interval (in ticks) between iterations as needed
    }
}
