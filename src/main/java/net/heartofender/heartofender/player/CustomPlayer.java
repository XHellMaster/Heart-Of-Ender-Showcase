package net.heartofender.heartofender.player;

import org.bukkit.entity.Player;
import net.heartofender.heartofender.utils.CustomConfig;


public class CustomPlayer {

    Player player;
    CustomConfig config;

    double currentMana;
    Integer maxMana;

    double currentHealth;
    Integer maxHealth;



    public CustomPlayer(Player p) {
        player = p;
        config = new CustomConfig("Player", p.getUniqueId().toString());;

        currentMana = Double.parseDouble(config.get("mana.current").toString());
        maxMana = (Integer) config.get("mana.max");

        currentHealth = Double.parseDouble(config.get("health.current").toString());
        maxHealth = (Integer) config.get("health.max");
    }


    // classes for spells

    public void knockback(double kbstrength) {
        if (kbstrength != 0) {
            player.setVelocity(player.getLocation().getDirection().multiply(-kbstrength));
        }
    }



    // called every second by CustomPlayerManager

    public void regen() {
        regenMana();
        regenHealth();
    }


    public void regenMana() {
        if (currentMana < maxMana) {
            if (currentMana > 500) {
                currentMana += maxMana / 20.0;
            } else {
                currentMana += maxMana / 17.0;
            }
        } else {
            // checks if it went over the max
            // also snaps back to max if it overflowed before manually or something
            currentMana = maxMana;
        }

        updateMana(currentMana);
    }

    public void regenHealth() {
        if (currentHealth < maxHealth) {
            if (currentHealth > 500) {
                currentHealth =+ maxHealth / 20.0;
            } else {
                currentHealth =+ maxHealth / 17.0;
            }
        } else {
            // same logic as in regenHealth
            currentHealth = maxHealth;
        }

        updateHealth(currentHealth);
    }



    // util

    public void showab() {
        player.sendActionBar("§dMana: " + currentMana + "    " + "§cHealth: " + currentHealth);
    }



    // updaters

    public void updateMana(double mana) {
        currentMana = mana;

        if (currentMana > maxMana) {
            currentMana = maxMana;
        }

        config.set("mana.current", mana);

        showab();
    }

    public void updateHealth(double health) {
        currentHealth = health;

        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }

        config.set("health.current", health);

        showab();
    }

    public void applyDamage(double damage) {
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
        if (currentHealth == 0.0) {
           player.sendMessage("you died, i can call you a slur now");
           updateHealth(50);
        }
    }

    public void heal(double health) {
        updateHealth(currentHealth + health);
    }

    public void saveConfig() {
        config.save();
    }




    // gets

    public double getMana() {
        return currentMana;
    }

    public double getHealth() {
        return currentHealth;
    }

    public double getCurrentConfigMana() {
        return Double.parseDouble(config.get("mana.current").toString());
    }

    public double getCurrentConfigHealth() {
        return Double.parseDouble(config.get("health.current").toString());
    }

    public double getMaxConfigMana() {
        return Double.parseDouble(config.get("mana.max").toString());
    }

    public double getMaxConfigHealth() {
        return Double.parseDouble(config.get("health.max").toString());
    }

    public Player getPlayer() {
        return player;
    }


}
