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

public class BlazeFury extends Spell {

    public BlazeFury() {
        super(Element.FIRE, Rarity.BASIC, 15, "Blaze Fury", "Summons a ring of fire around the player, damaging enemies who step inside.", SClassifier.DAMAGE, "net.heartofender.heartofender.features.magic.spells.damage.BlazeFury", 20);
    }

    public BlazeFury(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        Location center = player.getLocation();

        // Create the Blaze Fury effect
        new BukkitRunnable() {
            int ticks = 0;
            int duration = 10;
            int radius = 5;

            double centerx = center.getX();
            double centery = center.getY();
            double centerz = center.getZ();

            public void run() {
                ticks++;

                // Create a filled circle of fire around the player
                for (int i = 0; i <= radius; i++) {
                    for (double angle = 0; angle < 360; angle += 10) {
                        double x = centerx + i * Math.cos(Math.toRadians(angle));
                        double y = centery;
                        double z = centerz + i * Math.sin(Math.toRadians(angle));
                        Location loc = new Location(center.getWorld(), x, y, z);
                        center.getWorld().spawnParticle(Particle.FLAME, loc, 1);
                        if (!loc.getBlock().getType().isSolid()) {
                            loc.getBlock().setType(Material.FIRE);
                        }
                    }
                }

                // Damage players inside the circle every second
                for (Entity entity : center.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (entity instanceof Player && entity != player) {
                        Location entityLocation = entity.getLocation();
                        if (entityLocation.distance(center) <= radius) {
                            CustomPlayer customPlayer = new CustomPlayer((Player) entity);
                            customPlayer.applyDamage(1);
                        }
                    }
                }

                if (ticks >= duration * 20) { // Stop after the specified duration (in seconds)
                    // Remove all fire blocks after the duration is over
                    for (int i = 0; i <= radius; i++) {
                        for (double angle = 0; angle < 360; angle += 10) {
                            double x = centerx + i * Math.cos(Math.toRadians(angle));
                            double y = centery;
                            double z = centerz + i * Math.sin(Math.toRadians(angle));
                            Location loc = new Location(center.getWorld(), x, y, z);
                            if (loc.getBlock().getType() == Material.FIRE) {
                                loc.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Adjust the interval (in ticks) between iterations as needed

    }
}
