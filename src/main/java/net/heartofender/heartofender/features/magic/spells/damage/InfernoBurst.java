package net.heartofender.heartofender.features.magic.spells.damage;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InfernoBurst extends Spell {

    public InfernoBurst() {
        super(Element.FIRE, Rarity.BASIC, 15, "Inferno Burst", "A burst of flame that damages enemies in a cone in front of the player.", SClassifier.DAMAGE, "net.heartofender.heartofender.features.magic.spells.damage.InfernoBurst", 20);
    }

    public InfernoBurst(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        Location center = player.getLocation();

        // Create the Inferno Burst effect
        new BukkitRunnable() {
            int ticks = 0;
            int duration = 3;
            int maxDistance = 10;
            double coneAngle = Math.toRadians(45); // Cone angle in radians

            public void run() {
                ticks++;

                // Create a cone of fire in front of the player
                Vector direction = player.getLocation().getDirection();
                for (int distance = 1; distance <= maxDistance; distance++) {
                    for (double angle = -coneAngle; angle <= coneAngle; angle += Math.toRadians(5)) {
                        double x = center.getX() + distance * Math.cos(angle) * direction.getX();
                        double y = center.getY() + distance * Math.cos(angle) * direction.getY();
                        double z = center.getZ() + distance * Math.cos(angle) * direction.getZ();
                        Location loc = new Location(center.getWorld(), x, y, z);
                        center.getWorld().spawnParticle(Particle.FLAME, loc, 1);
                    }
                }

                // Damage enemies caught in the blast
                for (Entity entity : center.getWorld().getNearbyEntities(center, maxDistance, maxDistance, maxDistance)) {
                    if (entity instanceof Player && entity != player) {
                        Location entityLocation = entity.getLocation();
                        Vector entityDirection = entityLocation.subtract(center).toVector();
                        if (entityDirection.angle(direction) <= coneAngle) {
                            CustomPlayer customPlayer = new CustomPlayer((Player) entity);
                            customPlayer.applyDamage(1);
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
