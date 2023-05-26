package net.heartofender.heartofender.features.magic.spells.damage;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.*;
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

public class Tsunami extends Spell {

    public Tsunami() {
        super(Element.WATER, Rarity.ETHEREAL, 15, "Tidal Wave", "Summon a powerful wave of water that damages.", SClassifier.DAMAGE, "net.heartofender.heartofender.features.magic.spells.damage.Tsunami", 20);
    }

    public Tsunami(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        System.out.println("Command executed");
        final double knockbackStrength = 1.5;
        final int waveLength = 7;
        final double waveSpeed = 0.2;
        final double otherParam = 15;


        CustomPlayer caster = new CustomPlayer(player);
        Location location = caster.getPlayer().getLocation().add(0, 1, 0);

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= 3) {
                    cancel();
                    return;
                }

                new BukkitRunnable() {
                    double progress = 0;
                    double waveHeight = 8;
                    double waveWidth = 30;
                    double blockThrowStrength = 1.0;
                    List<FallingBlock> fallingBlocks = new ArrayList<>();
                    Vector direction = player.getLocation().getDirection().normalize();
                    Location initialLocation = player.getLocation().clone();

                    public void run() {
                        progress += waveSpeed;

                        double startX = initialLocation.getX() - direction.getZ() * waveWidth / 2;
                        double startZ = initialLocation.getZ() + direction.getX() * waveWidth / 2;
                        double startY = initialLocation.getY();

                        for (int i = 0; i < waveWidth; i++) {
                            double x = startX + direction.getZ() * i + direction.getX() * progress;
                            double z = startZ - direction.getX() * i + direction.getZ() * progress;
                            double y = startY + Math.sin((double) progress / waveLength * Math.PI) * waveHeight;

                            // Spawn particles from the top of the wave to the floor
                            for (double j = y; j >= startY; j -= 0.5) {
                                location.getWorld().spawnParticle(Particle.WATER_SPLASH, x, j, z, 10);
                            }

                            Block block = location.getWorld().getBlockAt(new Location(location.getWorld(), x, y - 1, z));
                            if (!block.getType().equals(Material.AIR)) {
                                FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(new Location(location.getWorld(), x, y, z), block.getBlockData());
                                fallingBlock.setDropItem(false);
                                fallingBlock.setVelocity(new Vector(Math.random() - 0.5, Math.random(), Math.random() - 0.5).multiply(blockThrowStrength));
                                NamespacedKey key = new NamespacedKey(Main.getInstance(), "tsunami");
                                NamespacedKey key1 = new NamespacedKey(Main.getInstance(), "spawner");
                                NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "damage");

                                fallingBlock.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 0);
                                fallingBlock.getPersistentDataContainer().set(key1, PersistentDataType.STRING, player.getUniqueId().toString());
                                fallingBlock.getPersistentDataContainer().set(key2, PersistentDataType.DOUBLE, otherParam);
                                fallingBlocks.add(fallingBlock);
                            }

                            location.getWorld().spawnParticle(Particle.WATER_SPLASH, x, startY, z, 1);
                        }

                        for (Entity entity : location.getWorld().getNearbyEntities(new Location(location.getWorld(), startX + direction.getZ() * waveWidth / 2, startY, startZ - direction.getX() * waveWidth / 2), waveLength, waveHeight, waveWidth)) {
                            if (entity instanceof Player && entity != player) {
                                CustomPlayer target = new CustomPlayer((Player) entity);
                                target.applyDamage(otherParam * 5);
                                target.knockback(knockbackStrength * 2);
                            }
                        }

                        if(progress >= 25) {
                            cancel();
                        }
                    }
                }.runTaskTimer(Main.getInstance(), 0, 2);

                count++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 30);

    }
}
