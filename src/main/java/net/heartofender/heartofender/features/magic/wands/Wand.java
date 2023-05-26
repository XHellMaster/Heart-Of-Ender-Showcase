package net.heartofender.heartofender.features.magic.wands;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.player.CustomPlayer;
import net.heartofender.heartofender.utils.CustomConfig;
import net.heartofender.heartofender.utils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static net.heartofender.heartofender.features.magic.MagicUtils.*;

public class Wand {

    Spell currentSpell; //current spell
    Spell currentRightSpell;
    Spell comboSpell;
    WandInventory spellGUI; //gui for the spells
    Element elementAffinity; //element that the wand is good with
    Element elementWeakness; //element that the wand is weak with
    ItemStack wandItem;
    Rarity rarity;
    int mana; //mana added to player mana
    double spellPower; // how powerful the wand is (more SP = more damage/healing etc.)
    double spellEfficiency; // mana usage
    double castSpeed; //how fast can the player cast a spell
    int numberUses; //how many uses of the wand (used to calculate wandAffinity)
    double wandAffinity; //scales with uses(player has to "learn/bond" with the wand, gets more powerful as the wand is used more)
    String name;
    private long lastCastTime = 0;

    private static HashMap<UUID, Long> timingManager = new HashMap<>();

    UUID wandID;
    CustomConfig wandConfig;

    public Wand(String name, int mana, double spellPower, double spellEfficiency, double castSpeed, Element strong, Element weak, Rarity r) {// used for a default wand

        this.mana = mana;
        this.spellPower = spellPower;
        this.spellEfficiency = spellEfficiency;
        this.castSpeed = castSpeed;
        elementAffinity = strong;
        elementWeakness = weak;
        rarity = r;
        wandID = UUID.randomUUID();
        spellGUI = new WandInventory(rarity, wandID);
        wandItem = new ItemStack(Material.STICK);
        ItemMeta wandMeta = wandItem.getItemMeta();
        wandMeta.setUnbreakable(true);
        List<String> lore = new ArrayList<>();
        String nameColor = getElementColor(elementAffinity);
        this.name = nameColor + name;
        wandMeta.setDisplayName(nameColor + name);
        String elementStrong = "§6Element Affinity: " + getElementName(elementAffinity);
        String elementWeak = "§4Element Hatred: " + getElementName(elementWeakness);
        String sP = "§5Spell Power: " + spellPower;
        String addMana = "§dMana: " + mana;
        String sE = "§dSpell Efficiency: " + spellEfficiency;
        String wA = "§eWand Affinity: " + (100 * wandAffinity);
        String speed = "§9Casting Speed: " + castSpeed;
        String rar = getRarityName(r);

        lore.add(elementStrong);
        lore.add(elementWeak);
        lore.add(sP);
        lore.add(addMana);
        lore.add(sE);
        lore.add(wA);
        lore.add(speed);
        lore.add(rar);
        wandMeta.setLore(lore);
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "wandID");
        wandMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, wandID.toString());

        wandItem.setItemMeta(wandMeta);

        wandConfig = new CustomConfig("Wands", wandID.toString());

        YamlConfiguration config = wandConfig.getConfig();

        config.set("wandID", wandID.toString());
        config.set("name", this.name);
        config.set("elementAffinity", elementAffinity.toString());
        config.set("elementWeakness", elementWeakness.toString());
        config.set("mana", mana);
        config.set("spellPower", spellPower);
        config.set("spellEfficiency", spellEfficiency);
        config.set("castSpeed", castSpeed);
        config.set("numberUses", numberUses);
        config.set("wandAffinity", wandAffinity);
        config.set("rarity", rarity.toString());
        config.set("contents", "placeholder");
        config.set("currentSpell", "placeholder");
        config.set("currentRightSpell", "placeholder");
        config.set("comboSpell", "placeholder");

        wandConfig.save();
    }

    //use to create wands from files
    public Wand(UUID wandID, ItemStack item) {

        YamlConfiguration config = FileUtils.getConfig("Wands", wandID.toString());
        wandConfig = new CustomConfig("Wands", wandID.toString());
        String name = config.getString("name");
        Element strong = Element.valueOf(config.getString("elementAffinity").replaceAll(" ", ""));
        Element weak = Element.valueOf(config.getString("elementWeakness").replaceAll(" ", ""));
        int mana = config.getInt("mana");
        double spellPower = config.getDouble("spellPower");
        double spellEfficiency = config.getDouble("spellEfficiency");
        double castSpeed = config.getDouble("castSpeed");
        int numberUses = config.getInt("numberUses");
        double wandAffinity = config.getDouble("wandAffinity");
        Rarity rarity = Rarity.valueOf(config.getString("rarity").replaceAll(" ", ""));
        this.wandID = wandID;
        this.name = name;
        elementAffinity = strong;
        elementWeakness = weak;
        this.mana = mana;
        this.spellPower = spellPower;
        this.spellEfficiency = spellEfficiency;
        this.castSpeed = castSpeed;
        this.numberUses = numberUses;
        this.wandAffinity = wandAffinity;
        this.rarity = rarity;
        this.wandItem = item;

        spellGUI = new WandInventory(rarity, wandID);
        currentSpell = null;
        currentRightSpell = null;
        comboSpell = null;


        NamespacedKey key = new NamespacedKey(Main.getInstance(), "currentSpell");
        NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "currentRightSpell");
        NamespacedKey key3 = new NamespacedKey(Main.getInstance(), "comboSpell");
        spellGUI = new WandInventory(rarity, wandID);
        if (!wandItem.getItemMeta().getPersistentDataContainer().has(key)) {
            currentSpell = null;
        } else {
            try {
                ConfigurationSection section = config.getConfigurationSection("currentSpell");
                if (section != null) {
                    Set<String> keys = section.getKeys(false);
                    if (!keys.isEmpty()) {
                        String spellIdString = keys.iterator().next();
                        UUID spellID = UUID.fromString(spellIdString);
                        currentSpell = Spell.deserializeForWand(wandID, spellID, Cast.LEFT);
                        currentSpell.serializeForWand(wandID, Cast.LEFT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!wandItem.getItemMeta().getPersistentDataContainer().has(key2)) {
            currentRightSpell = null;
        } else {
            try {
                ConfigurationSection section = config.getConfigurationSection("currentRightSpell");
                if (section != null) {
                    Set<String> keys = section.getKeys(false);
                    if (!keys.isEmpty()) {
                        String spellIdString = keys.iterator().next();
                        UUID spellID = UUID.fromString(spellIdString);
                        currentRightSpell = Spell.deserializeForWand(wandID, spellID, Cast.RIGHT);
                        currentRightSpell.serializeForWand(wandID, Cast.RIGHT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!wandItem.getItemMeta().getPersistentDataContainer().has(key3)) {
            comboSpell = null;
        } else {
            try {
                ConfigurationSection section = config.getConfigurationSection("comboSpell");
                if (section != null) {
                    Set<String> keys = section.getKeys(false);
                    if (!keys.isEmpty()) {
                        String spellIdString = keys.iterator().next();
                        UUID spellID = UUID.fromString(spellIdString);
                        comboSpell = Spell.deserializeForWand(wandID, spellID, Cast.COMBO);
                        comboSpell.serializeForWand(wandID, Cast.COMBO);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public ItemStack getWandItem() {
        return wandItem;
    }

    //handle casting

    public Rarity getRarity() {
        return rarity;
    }
    public void cast(CustomPlayer p, Cast c) {
        long currentTime = System.currentTimeMillis();
        double cooldown = 1000.0 * castSpeed;


        Long lastUseTime = Wand.timingManager.get(wandID);
        if (lastUseTime != null && currentTime - lastUseTime < cooldown) {
            p.getPlayer().sendMessage(ChatColor.RED + "Your wand is still on cooldown!");
            return;
        }
        //left cast
        if (c.equals(Cast.LEFT)) {
            try {
                UUID spellID = UUID.fromString((String) wandConfig.getConfig().getConfigurationSection("currentSpell").getValues(false).keySet().toArray()[0]);
                currentSpell = Spell.deserializeForWand(wandID, spellID, Cast.LEFT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentSpell == null) return;
            SClassifier sc = currentSpell.getType();
            if (sc.equals(SClassifier.DAMAGE)) {
                currentSpell.calculateDamage(elementAffinity, elementWeakness, spellPower, wandAffinity, rarity);
            }

            if (p.getMana() < currentSpell.getManaCost()) {
                p.getPlayer().sendMessage(ChatColor.RED + "You don't have enough mana to cast this spell!");
                return;
            }
            p.updateMana(p.getMana() - currentSpell.getManaCost());
            currentSpell.cast(p.getPlayer());
            p.getPlayer().sendMessage(ChatColor.GREEN + "You cast " + currentSpell.getColoredName() + ChatColor.GREEN  + " for " + ChatColor.LIGHT_PURPLE + "" + currentSpell.getManaCost() + " Mana!");
        }
        //right cast
        else if (c.equals(Cast.RIGHT)) {
            try {
                UUID spellID = UUID.fromString((String) wandConfig.getConfig().getConfigurationSection("currentRightSpell").getValues(false).keySet().toArray()[0]);
                currentRightSpell = Spell.deserializeForWand(wandID, spellID, Cast.RIGHT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentRightSpell == null) return;
            SClassifier sc = currentRightSpell.getType();
            if (sc.equals(SClassifier.DAMAGE)) {
                currentRightSpell.calculateDamage(elementAffinity, elementWeakness, spellPower, wandAffinity, rarity);
            }

            if (p.getMana() < currentRightSpell.getManaCost()) {
                p.getPlayer().sendMessage(ChatColor.RED + "You don't have enough mana to cast this spell!");
                return;
            }
            p.updateMana(p.getMana() - currentRightSpell.getManaCost());

            currentRightSpell.cast(p.getPlayer());
            p.getPlayer().sendMessage(ChatColor.GREEN + "You cast " + currentRightSpell.getColoredName() + ChatColor.GREEN  + " for " + ChatColor.LIGHT_PURPLE + "" + currentRightSpell.getManaCost() + " Mana!");

        }
        //combo cast
        else if (c.equals(Cast.COMBO)) {
            try {
                comboSpell.serializeForWand(wandID, Cast.COMBO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            comboSpell = WandInventory.getCombinationSpell(wandID, rarity, p.getPlayer(), 0);

            SClassifier sc = comboSpell.getType();
            if (sc.equals(SClassifier.DAMAGE)) {
                comboSpell.calculateDamage(elementAffinity, elementWeakness, spellPower, wandAffinity, rarity);
            }

            if (p.getMana() < comboSpell.getManaCost()) {
                p.getPlayer().sendMessage(ChatColor.RED + "You don't have enough mana to cast this spell!");
                return;
            }
            p.updateMana(p.getMana() - comboSpell.getManaCost());

            comboSpell.cast(p.getPlayer());
            p.getPlayer().sendMessage(ChatColor.GREEN + "You cast " + comboSpell.getColoredName() + ChatColor.GREEN  + " for " + ChatColor.LIGHT_PURPLE + "" + comboSpell.getManaCost() + " Mana!");

        }
        //replace redundant code later into a function with all SClassifiers
        numberUses++;
        lastCastTime = currentTime;
        timingManager.put(wandID, currentTime);
    }

    public void cycleSpell(Player p) {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "currentSpell");
        List<Spell> spells = WandInventory.getSpells(wandID, rarity, p);
        if (spells.size() > 0) {
            int currentIndex = -1;
            for (int i = 0; i < spells.size(); i++) {
                if (currentSpell != null && spells.get(i).getID().toString().equals(currentSpell.getID().toString())) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex == -1) {
                currentSpell = spells.get(0);
            } else {
                int nextIndex = (currentIndex + 1) % spells.size();
                currentSpell = spells.get(nextIndex);
            }
            ItemMeta meta = wandItem.getItemMeta();
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, currentSpell.getID().toString());
            wandItem.setItemMeta(meta);
            try {
                currentSpell.serializeForWand(wandID, Cast.LEFT);

                p.sendMessage(ChatColor.GREEN + "Your current left click spell is now: " + currentSpell.getColoredName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            p.sendMessage(ChatColor.RED + "You currently have no left click spells equipped!");
        }
    }

    public void cycleRightSpell(Player p) {
        List<Spell> spells = WandInventory.getRightSpells(wandID, rarity, p, 0);
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "currentRightSpell");
        if (spells.size() > 0) {
            int currentIndex = -1;
            for (int i = 0; i < spells.size(); i++) {
                if (currentRightSpell != null && spells.get(i).getID().toString().equals(currentRightSpell.getID().toString())) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex == -1) {
                currentRightSpell = spells.get(0);
            } else {
                int nextIndex = (currentIndex + 1) % spells.size();
                currentRightSpell = spells.get(nextIndex);
            }
            ItemMeta meta = wandItem.getItemMeta();
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, currentRightSpell.getID().toString());
            wandItem.setItemMeta(meta);
            try {
                currentRightSpell.serializeForWand(wandID, Cast.RIGHT);
                p.sendMessage(ChatColor.GREEN + "Your current right click spell is now: " + currentRightSpell.getColoredName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            p.sendMessage(ChatColor.RED + "You currently have no right click spells equipped!");
        }
    }
    public void open(Player p) {

        spellGUI.clean(p);
        spellGUI.populate();
        p.openInventory(spellGUI.getInventory());
    }

    public static Wand getWandFromItem(UUID wandID, ItemStack item) {
        return new Wand(wandID, item);
    }
}
