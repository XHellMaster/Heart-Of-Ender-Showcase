package net.heartofender.heartofender.features.magic.spells.combinations;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Typhoon extends Spell {

    public Typhoon() {
        super(Element.VORTEX, Rarity.ENCHANTED, 20, "Typhoon", "Pulls enemies in and shoots them out, dealing damage.", SClassifier.DAMAGE, "net.heartofender.heartofender.features.magic.spells.combinations.Typhoon", 20);
    }

    public Typhoon(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        Location center = player.getLocation();

        // Create the combination effect
        new BukkitRunnable() {
            int ticks = 0;
            int duration = 10;
            int radius = 5;
            double pullStrength = 0.3;
            double knockbackStrength = 1.5;

            public void run() {
                ticks++;

                // Create a whirlpool using blue particles
                for (double angle = 0; angle < 360; angle += 10) {
                    double x = center.getX() + radius * Math.cos(Math.toRadians(angle));
                    double y = center.getY();
                    double z = center.getZ() + radius * Math.sin(Math.toRadians(angle));
                    Location loc = new Location(center.getWorld(), x, y, z);
                    center.getWorld().spawnParticle(Particle.WATER_SPLASH, loc, 1);
                    center.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(0, 0, 139), 1));
                }

                // Pull enemies towards the center, then damage them and knock them back in a random direction
                for (Entity entity : center.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (entity instanceof Player && entity != player) {
                        Location entityLocation = entity.getLocation();
                        double distance = entityLocation.distance(center);

                        if (distance > 1) {
                            // Pull the entity towards the center
                            double pullFactor = Math.min(pullStrength / distance, 1);
                            double dx = (center.getX() - entityLocation.getX()) * pullFactor;
                            double dy = (center.getY() - entityLocation.getY()) * pullFactor;
                            double dz = (center.getZ() - entityLocation.getZ()) * pullFactor;
                            entity.setVelocity(entity.getVelocity().add(new Vector(dx, dy, dz)));
                        }

                        if (distance <= 1) {
                            // Damage the entity and knock it back
                            CustomPlayer customPlayer = new CustomPlayer((Player) entity);
                            customPlayer.applyDamage(1);
                            Vector knockbackDirection = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();


                            Vector kbvec = knockbackDirection.multiply(knockbackStrength);
                            ((Player) entity).knockback(
                                    Math.abs(kbvec.getX()),
                                    Math.abs(kbvec.getY()),
                                    Math.abs(kbvec.getZ())
                            );
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
