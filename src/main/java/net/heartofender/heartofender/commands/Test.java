package net.heartofender.heartofender.commands;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.features.magic.spells.combinations.Typhoon;
import net.heartofender.heartofender.features.magic.spells.damage.*;
import net.heartofender.heartofender.features.magic.wands.Wand;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("altar")) {
            Player player = (Player) commandSender;
            ItemStack altarBlock = new ItemStack(Material.ENCHANTING_TABLE);
            ItemMeta meta = altarBlock.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(Main.getInstance(), "isAltar"), PersistentDataType.BYTE, (byte) 1);
            String name;
            if (args.length > 0 && args[0].equalsIgnoreCase("enhanced")) {
                data.set(new NamespacedKey(Main.getInstance(), "altarType"), PersistentDataType.STRING, "enhanced");
                name = ChatColor.BLUE + "Enhanced Altar";
            } else {
                data.set(new NamespacedKey(Main.getInstance(), "altarType"), PersistentDataType.STRING, "basic");
                name = ChatColor.LIGHT_PURPLE + "Basic Altar";
            }
            meta.setDisplayName(name);
            altarBlock.setItemMeta(meta);
            player.getInventory().addItem(altarBlock);
            return true;
        }

        if (command.getName().equalsIgnoreCase("test")){
            Player p = (Player) commandSender;
            // Water Wands
            Wand drownedMarinersStick = new Wand("Drowned Mariner's Stick", 50, 0.3, 0.5, 3.0, Element.WATER, Element.FIRE, Rarity.CURSED);
            Wand novicesAquaRod = new Wand("Novice's Aqua Rod", 100, 0.7, 0.75, 2.5, Element.WATER, Element.EARTH, Rarity.BASIC);
            Wand seaWhisperersStaff = new Wand("Sea Whisperer's Staff", 150, 1.2, 1.0, 2.0, Element.WATER, Element.AIR, Rarity.ENCHANTED);
            Wand oceanlordsScepter = new Wand("Oceanlord's Scepter", 250, 2.0, 1.5, 1.5, Element.WATER, Element.LIGHT, Rarity.GRAND);
            Wand nymphsBaneOfMist = new Wand("Nymph's Bane of Mist", 400, 3.0, 2.0, 1.0, Element.WATER, Element.DARKNESS, Rarity.ETHEREAL);
            Wand tritonsTridentOfTides = new Wand("Triton's Trident of Tides", 600, 4.0, 2.5, 0.5, Element.WATER, Element.FIRE, Rarity.CELESTIAL);

            Spell tidal = new Tsunami();
            Spell whirl = new Whirlpool();
            Spell typhoon = new Typhoon();
            Spell lightningStrike = new LightningStrike();
            Spell inferno = new InfernoBurst();
            Spell gale = new GaleForce();
            Spell fury = new BlazeFury();
            Spell firewalker = new Firewalker();
            Spell healingrain = new HealingRain();
            /*
            // Fire Wands
            Wand ashenedTwig = new Wand("Ashened Twig", 50, 0.3, 0.5, 3.0, Element.FIRE, Element.WATER, Rarity.CURSED);
            Wand novicePyroStick = new Wand("Novice's Pyro Stick", 100, 0.7, 0.75, 2.5, Element.FIRE, Element.AIR, Rarity.BASIC);
            Wand flamekeepersWand = new Wand("Flamekeeper's Wand", 150, 1.2, 1.0, 2.0, Element.FIRE, Element.EARTH, Rarity.ENCHANTED);
            Wand infernoKingsCudgel = new Wand("Inferno King's Cudgel", 250, 2.0, 1.5, 1.5, Element.FIRE, Element.DARKNESS, Rarity.GRAND);
            Wand phoenixsFlare = new Wand("Phoenix's Flare", 400, 3.0, 2.0, 1.0, Element.FIRE, Element.LIGHT, Rarity.ETHEREAL);
            Wand draconicBlazeRod = new Wand("Draconic Blaze Rod", 600, 4.0, 2.5, 0.5, Element.FIRE, Element.WATER, Rarity.CELESTIAL);

            // Light Wands
            Wand dimLantern = new Wand("Dim Lantern", 50, 0.3, 0.5, 3.0, Element.LIGHT, Element.DARKNESS, Rarity.CURSED);
            Wand novicesGleamingRod = new Wand("Novice's Gleaming Rod", 100, 0.7, 0.75, 2.5, Element.LIGHT, Element.FIRE, Rarity.BASIC);
            Wand luminousScepter = new Wand("Luminous Scepter", 150, 1.2, 1.0, 2.0, Element.LIGHT, Element.WATER, Rarity.ENCHANTED);
            Wand dawnBringersStaff = new Wand("Dawn Bringer's Staff", 250, 2.0, 1.5, 1.5, Element.LIGHT, Element.AIR, Rarity.GRAND);
            Wand seraphsRadiantWand = new Wand("Seraph's Radiant Wand", 400, 3.0, 2.0, 1.0, Element.LIGHT, Element.EARTH, Rarity.ETHEREAL);
            Wand celestialBeacon = new Wand("Celestial Beacon", 600, 4.0, 2.5, 0.5, Element.LIGHT, Element.DARKNESS, Rarity.CELESTIAL);
            // Air Wands
            Wand fleetingZephyrTwig = new Wand("Fleeting Zephyr Twig", 50, 0.3, 0.5, 3.0, Element.AIR, Element.EARTH, Rarity.CURSED);
            Wand novicesGustRod = new Wand("Novice's Gust Rod", 100, 0.7, 0.75, 2.5, Element.AIR, Element.WATER, Rarity.BASIC);
            Wand windcallersStaff = new Wand("Windcaller's Staff", 150, 1.2, 1.0, 2.0, Element.AIR, Element.FIRE, Rarity.ENCHANTED);
            Wand skywardensScepter = new Wand("Skywarden's Scepter", 250, 2.0, 1.5, 1.5, Element.AIR, Element.DARKNESS, Rarity.GRAND);
            Wand sylphsGaleWand = new Wand("Sylph's Gale Wand", 400, 3.0, 2.0, 1.0, Element.AIR, Element.LIGHT, Rarity.ETHEREAL);
            Wand zephyrusBreezeRod = new Wand("Zephyrus' Breeze Rod", 600, 4.0, 2.5, 0.5, Element.AIR, Element.EARTH, Rarity.CELESTIAL);

            // Earth Wands
            Wand splinteredRoot = new Wand("Splintered Root", 50, 0.3, 0.5, 3.0, Element.EARTH, Element.AIR, Rarity.CURSED);
            Wand novicesTerraStaff = new Wand("Novice's Terra Staff", 100, 0.7, 0.75, 2.5, Element.EARTH, Element.FIRE, Rarity.BASIC);
            Wand geomancersRod = new Wand("Geomancer's Rod", 150, 1.2, 1.0, 2.0, Element.EARTH, Element.WATER, Rarity.ENCHANTED);
            Wand earthshapersCudgel = new Wand("Earthshaper's Cudgel", 250, 2.0, 1.5, 1.5, Element.EARTH, Element.LIGHT, Rarity.GRAND);
            Wand gaiasMight = new Wand("Gaia's Might", 400, 3.0, 2.0, 1.0, Element.EARTH, Element.DARKNESS, Rarity.ETHEREAL);
            Wand titanicTectonicStaff = new Wand("Titanic Tectonic Staff", 600, 4.0, 2.5, 0.5, Element.EARTH, Element.AIR, Rarity.CELESTIAL);

            // Darkness Wands

            Wand gloomTwig = new Wand("Gloom Twig", 50, 0.3, 0.5, 3.0, Element.DARKNESS, Element.LIGHT, Rarity.CURSED);
            Wand novicesShadowStick = new Wand("Novice's Shadow Stick", 100, 0.7, 0.75, 2.5, Element.DARKNESS, Element.AIR, Rarity.BASIC);
            Wand nightweaversWand = new Wand("Nightweaver's Wand", 150, 1.2, 1.0, 2.0, Element.DARKNESS, Element.EARTH, Rarity.ENCHANTED);
            Wand umbraKingsScepter = new Wand("Umbra King's Scepter", 250, 2.0, 1.5, 1.5, Element.DARKNESS, Element.WATER, Rarity.GRAND);
            Wand abyssalNexus = new Wand("Abyssal Nexus", 400, 3.0, 2.0, 1.0, Element.DARKNESS, Element.FIRE, Rarity.ETHEREAL);
            Wand voidEmperorsRod = new Wand("Void Emperor's Rod", 600, 4.0, 2.5, 0.5, Element.DARKNESS, Element.LIGHT, Rarity.CELESTIAL);

            // Add the wands to player's inventory
            */
            p.getInventory().addItem(drownedMarinersStick.getWandItem());
            p.getInventory().addItem(novicesAquaRod.getWandItem());
            p.getInventory().addItem(seaWhisperersStaff.getWandItem());
            p.getInventory().addItem(oceanlordsScepter.getWandItem());
            p.getInventory().addItem(nymphsBaneOfMist.getWandItem());
            p.getInventory().addItem(tritonsTridentOfTides.getWandItem());
            p.getInventory().addItem(tidal.getSpellItem());
            p.getInventory().addItem(whirl.getSpellItem());
            p.getInventory().addItem(typhoon.getSpellItem());
            p.getInventory().addItem(lightningStrike.getSpellItem());
            p.getInventory().addItem(inferno.getSpellItem());
            p.getInventory().addItem(gale.getSpellItem());
            p.getInventory().addItem(fury.getSpellItem());
            p.getInventory().addItem(firewalker.getSpellItem());
            p.getInventory().addItem(healingrain.getSpellItem());

            /*
            p.getInventory().addItem(ashenedTwig.getWandItem());
            p.getInventory().addItem(novicePyroStick.getWandItem());
            p.getInventory().addItem(flamekeepersWand.getWandItem());
            p.getInventory().addItem(infernoKingsCudgel.getWandItem());
            p.getInventory().addItem(phoenixsFlare.getWandItem());
            p.getInventory().addItem(draconicBlazeRod.getWandItem());

            p.getInventory().addItem(dimLantern.getWandItem());
            p.getInventory().addItem(novicesGleamingRod.getWandItem());
            p.getInventory().addItem(luminousScepter.getWandItem());
            p.getInventory().addItem(dawnBringersStaff.getWandItem());
            p.getInventory().addItem(seraphsRadiantWand.getWandItem());
            p.getInventory().addItem(celestialBeacon.getWandItem());

            p.getInventory().addItem(fleetingZephyrTwig.getWandItem());
            p.getInventory().addItem(novicesGustRod.getWandItem());
            p.getInventory().addItem(windcallersStaff.getWandItem());
            p.getInventory().addItem(skywardensScepter.getWandItem());
            p.getInventory().addItem(sylphsGaleWand.getWandItem());
            p.getInventory().addItem(zephyrusBreezeRod.getWandItem());

            p.getInventory().addItem(splinteredRoot.getWandItem());
            p.getInventory().addItem(novicesTerraStaff.getWandItem());
            p.getInventory().addItem(geomancersRod.getWandItem());
            p.getInventory().addItem(earthshapersCudgel.getWandItem());
            p.getInventory().addItem(gaiasMight.getWandItem());
            p.getInventory().addItem(titanicTectonicStaff.getWandItem());

            p.getInventory().addItem(gloomTwig.getWandItem());
            p.getInventory().addItem(novicesShadowStick.getWandItem());
            p.getInventory().addItem(nightweaversWand.getWandItem());
            p.getInventory().addItem(umbraKingsScepter.getWandItem());
            p.getInventory().addItem(abyssalNexus.getWandItem());
            p.getInventory().addItem(voidEmperorsRod.getWandItem());
            */
            return true;
        }

        if (command.getName().equalsIgnoreCase("tidalwave")) {
            Player player = (Player) commandSender;
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

        if (command.getName().equalsIgnoreCase("whirlpool")) {
            Player player = (Player) commandSender;
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

        if (command.getName().equalsIgnoreCase("blazefury")) {
            Player player = (Player) commandSender;
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

        if (command.getName().equalsIgnoreCase("galeforce")) {
            Player player = (Player) commandSender;
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

                    if (ticks >= duration * 10) { // Stop after the specified duration (in seconds)
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 10); // Adjust the interval (in ticks) between iterations as needed
        };

        if (command.getName().equalsIgnoreCase("typhoon")) {
            Player player = (Player) commandSender;
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

                    // Create a gust of wind using white particles
                    for (double angle = 0; angle < 360; angle += 10) {
                        double x = center.getX() + radius * Math.cos(Math.toRadians(angle));
                        double y = center.getY();
                        double z = center.getZ() + radius * Math.sin(Math.toRadians(angle));
                        Location loc = new Location(center.getWorld(), x, y, z);
                        center.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0.5, 0.5, 0.5, 0.1);
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

                                // Damage the entity and knock it back in a random direction
                                CustomPlayer customPlayer = new CustomPlayer((Player) entity);
                                customPlayer.applyDamage(1);
                                Vector knockbackDirection = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();

                                Vector kbvec = knockbackDirection.multiply(knockbackStrength);
                                ((Player) entity).knockback(kbvec.getX(), kbvec.getY(), kbvec.getZ());
                            }
                        }
                    }

                    if (ticks >= duration * 20) { // Stop after the specified duration (in seconds)
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 0, 20); // Adjust the interval (in ticks) between iterations as needed
        }



        return false;
    }
}
