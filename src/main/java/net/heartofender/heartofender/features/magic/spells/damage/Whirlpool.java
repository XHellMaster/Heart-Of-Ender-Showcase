package net.heartofender.heartofender.features.magic.spells.damage;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Whirlpool extends Spell {
    private final double pullStrength;
    private final int duration = 5;
    private final int radius = 5;

    public Whirlpool() {
        super(Element.WATER, Rarity.ENCHANTED, 20, "Whirlpool", "Creates a vortex of water that pulls enemies towards its center.", SClassifier.UTILITY, "net.heartofender.heartofender.features.magic.spells.damage.Whirlpool", 20);
        this.pullStrength = 0.3;
    }

    public Whirlpool(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
        this.pullStrength = 0.3;
    }

    @Override
    public void cast(Player player) {
        Location center = player.getLocation();

        // Create the whirlpool effect
        new BukkitRunnable() {
            int ticks = 0;
            int duration = 5;
            double pullStrength = 0.3;
            int maxRadius = 5;
            int swirls = 3;

            public void run() {
                ticks++;

                // Create multiple swirls
                for (int j = 0; j < swirls; j++) {
                    // Create a spiral pattern for each swirl
                    for (int i = 0; i < maxRadius; i++) {
                        double angle = ticks * (2 * Math.PI / 20) + i * (2 * Math.PI / maxRadius) + j * (2 * Math.PI / swirls);
                        double x = center.getX() + i * Math.cos(angle);
                        double y = center.getY() + i * 0.1; // Raise the particles off the ground based on their distance from the center
                        double z = center.getZ() + i * Math.sin(angle);
                        center.getWorld().spawnParticle(Particle.WATER_SPLASH, x, y, z, 1);
                        center.getWorld().spawnParticle(Particle.REDSTONE, x, y, z, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(0, 0, 139), 1));
                    }
                }

                // Pull nearby entities towards the center
                for (Entity entity : center.getWorld().getNearbyEntities(center, maxRadius, maxRadius, maxRadius)) {
                    if (entity instanceof Player && entity != player) {
                        Location targetLocation = entity.getLocation();
                        double distance = targetLocation.distance(center);

                        if (distance > 1) {
                            double pullFactor = Math.min(pullStrength / distance, 1);
                            double dx = (center.getX() - targetLocation.getX()) * pullFactor;
                            double dy = (center.getY() - targetLocation.getY()) * pullFactor;
                            double dz = (center.getZ() - targetLocation.getZ()) * pullFactor;

                            entity.setVelocity(entity.getVelocity().add(new Vector(dx, dy, dz)));
                        }
                    }
                }

                if (ticks >= duration * 20) { // Stop after the specified duration (in seconds)
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 1); // Adjust the interval (in ticks) between iterations as needed
    }
}
