package net.heartofender.heartofender.features.magic.spells.damage;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class HealingRain extends Spell {

    public HealingRain() {
        super(Element.WATER, Rarity.GRAND, 25, "Healing Rain", "A gentle rain that heals the player and their allies over time.", SClassifier.HEAL, "net.heartofender.heartofender.features.magic.spells.damage.HealingRain", 23);
    }

    public HealingRain(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        Location center = player.getLocation();

        // Create the Healing Rain effect
        new BukkitRunnable() {
            int ticks = 0;
            int duration = 10;
            int radius = 15;
            int height = 25;

            public void run() {
                ticks++;

                // Create a rain of particles above the player
                for (double angle = 0; angle < 360; angle += 10) {
                    for (int h = 0; h <= height; h++) {
                        double x = center.getX() + radius * Math.cos(Math.toRadians(angle));
                        double y = center.getY() + h;
                        double z = center.getZ() + radius * Math.sin(Math.toRadians(angle));
                        Location loc = new Location(center.getWorld(), x, y, z);
                        center.getWorld().spawnParticle(Particle.WATER_DROP, loc, 1);
                    }
                }

                // Heal all players within range
                for (Entity entity : center.getWorld().getNearbyEntities(center, radius, height, radius)) {
                    if (entity instanceof Player) {
                        CustomPlayer customPlayer = new CustomPlayer((Player) entity);
                        customPlayer.heal(1);
                    }
                }

                if (ticks >= duration * 20) { // Stop after the specified duration (in seconds)
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Adjust the interval (in ticks) between iterations as needed
    }
}
