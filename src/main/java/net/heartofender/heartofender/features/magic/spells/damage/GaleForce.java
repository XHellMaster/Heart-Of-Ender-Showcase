package net.heartofender.heartofender.features.magic.spells.damage;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;



public class GaleForce extends Spell {

    public GaleForce() {
        super(Element.AIR, Rarity.BASIC, 15, "Gale Force", "A powerful gust of wind that pushes enemies back and deals damage.", SClassifier.DAMAGE, "net.heartofender.heartofender.features.magic.spells.damage.GaleForce", 20);
    }

    public GaleForce(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        Location center = player.getLocation();

        // Create the Gale Force effect
        new BukkitRunnable() {
            int ticks = 0;
            int duration = 5;
            int radius = 5;
            double knockbackStrength = 1.5;

            public void run() {
                ticks++;

                // Create a gust of wind using particles
                for (double angle = 0; angle < 360; angle += 10) {
                    double x = center.getX() + radius * Math.cos(Math.toRadians(angle));
                    double y = center.getY();
                    double z = center.getZ() + radius * Math.sin(Math.toRadians(angle));
                    Location loc = new Location(center.getWorld(), x, y, z);
                    center.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0.5, 0.5, 0.5, 0.1);
                }

                // Push enemies back and deal damage
                for (Entity entity : center.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (entity instanceof Player && entity != player) {
                        Location entityLocation = entity.getLocation();
                        if (entityLocation.distance(center) <= radius) {
                            CustomPlayer customPlayer = new CustomPlayer((Player) entity);
                            customPlayer.applyDamage(1);

                            // Calculate knockback direction (from the center to the entity)
                            Vector knockbackDirection = entityLocation.subtract(center).toVector().normalize();
                            customPlayer.knockback(knockbackStrength);
                        }
                    }
                }

                if (ticks >= duration * 20) { // Stop after the specified duration (in seconds)
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Adjust the interval (in ticks) between iterations as needed

    }
}
