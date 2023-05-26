package net.heartofender.heartofender.features.magic.combination;

import com.jeff_media.customblockdata.CustomBlockData;
import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.spells.Spell;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public abstract class Altar {

    protected ItemStack[] inventory;
    protected ItemStack combineSlot;
    protected ItemStack spellSlot;
    protected ItemStack buttonSlot;
    protected ItemStack unusableSlot;

    public Altar() {
        this.inventory = new ItemStack[45];

        // Initialize combine slot
        this.combineSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta combineMeta = this.combineSlot.getItemMeta();
        combineMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "???");
        this.combineSlot.setItemMeta(combineMeta);

        // Initialize spell slot
        this.spellSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta spellMeta = this.spellSlot.getItemMeta();
        spellMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Spell");
        this.spellSlot.setItemMeta(spellMeta);

        // Initialize combine button
        this.buttonSlot = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta buttonMeta = this.buttonSlot.getItemMeta();
        buttonMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Combine");
        this.buttonSlot.setItemMeta(buttonMeta);

        // Initialize unusable slot
        this.unusableSlot = new ItemStack(Material.PURPLE_STAINED_GLASS);
        ItemMeta umeta = this.unusableSlot.getItemMeta();
        umeta.setDisplayName(ChatColor.DARK_RED + "Unusable Slot");
        this.unusableSlot.setItemMeta(umeta);

        // Fill inventory with unusable slots
        for (int i = 0; i < this.inventory.length; i++) {
            this.inventory[i] = this.unusableSlot;
        }

        // Set specific slots
        this.inventory[4] = this.combineSlot;
        this.inventory[40] = this.buttonSlot;
    }

    public static boolean isAltar(ItemStack item) {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "isAltar");
        return item.getItemMeta().getPersistentDataContainer().has(key);
    }
    public static boolean isAltar(Block item) {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "isAltar");
        return new CustomBlockData(item, Main.getInstance()).has(key);
    }

    public static String getAltarType(ItemStack item) {
        if (!isAltar(item)) {
            return null;
        }
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "altarType"), PersistentDataType.STRING)) {
            return data.get(new NamespacedKey(Main.getInstance(), "altarType"), PersistentDataType.STRING);
        }

        return null;
    }
    public static String getAltarType(Block item) {
        if (!isAltar(item)) {
            return null;
        }
        PersistentDataContainer data = new CustomBlockData(item, Main.getInstance());
        if (data.has(new NamespacedKey(Main.getInstance(), "altarType"), PersistentDataType.STRING)) {
            return data.get(new NamespacedKey(Main.getInstance(), "altarType"), PersistentDataType.STRING);
        }

        return null;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public abstract boolean canCombine(List<Spell> spells);

    public abstract void combine(boolean success, Player p);
}