package net.heartofender.heartofender.features.magic.wands;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;

public class WandInventory {
    private Rarity rarity;
    private Inventory inventory;
    private int leftClickIndex = 0;
    private int rightClickIndex = 0;
    private int size = 0;
    private UUID id;

    public WandInventory(Rarity rarity, UUID id) {
        this.rarity = rarity;
        this.id = id;
        // Determine the size and layout of the inventory based on the rarity
        switch (rarity) {
            case BASIC:
            case CURSED:
                size = 9;
                clean(null);
                break;
            case ENCHANTED:
                size = 18;
                clean(null);
                break;
            case GRAND:
                size = 27;
                clean(null);
                break;
            case ETHEREAL:
                size = 36;
                clean(null);
                break;
            case CELESTIAL:
                size = 45;
                clean(null);
                break;
        }
    }


    public Inventory clean(Player p) {
        Inventory inv = createInventory(p, size);
        inventory = inv;
        return inventory;
    }

    public void populate() {
        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());
        ConfigurationSection contents = config.getConfigurationSection("contents");

        if (contents != null) {
            for (String key : contents.getKeys(false)) {
                try {
                    UUID spellID = UUID.fromString(key);
                    Spell spell = Spell.deserialize(id, spellID);
                    inventory.setItem(spell.getSlot(), spell.getSpellItem());
                } catch (Exception e) {
                    // handle exception
                    e.printStackTrace();
                }
            }
        }
    }

    private Inventory createInventory(Player p, int size) {
        Inventory inv = Bukkit.createInventory(p, size, "Wand Spells");


        ItemStack combineSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta combineMeta = combineSlot.getItemMeta();
        List<String> combLore = new ArrayList<>();
        combineMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Spell Combine Slot");
        String desc = ChatColor.DARK_PURPLE + "Put spells in these slots to use combination spells!";
        combLore.add(desc);
        combineMeta.setLore(combLore);
        combineSlot.setItemMeta(combineMeta);

        ItemStack leftSlot = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta leftMeta = leftSlot.getItemMeta();
        List<String> leftLore = new ArrayList<>();
        leftMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Left Click Spell");
        String desc1 = ChatColor.DARK_PURPLE + "Put spells in these slots to use when left clicking!";
        combLore.add(desc1);
        leftMeta.setLore(leftLore);
        leftSlot.setItemMeta(leftMeta);

        ItemStack rightSlot = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta rightMeta = rightSlot.getItemMeta();
        List<String> rightLore = new ArrayList<>();
        rightMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Right Click Spell");
        String desc2 = ChatColor.DARK_PURPLE + "Put spells in these slots to use when right clicking!";
        combLore.add(desc2);
        rightMeta.setLore(rightLore);
        rightSlot.setItemMeta(rightMeta);

        ItemStack storageSlot = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta storageMeta = storageSlot.getItemMeta();
        List<String> storageLore = new ArrayList<>();
        storageMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Storage Slot");
        String desc3 = ChatColor.DARK_PURPLE + "Put spells in these slots to store them for the future!";
        combLore.add(desc3);
        storageMeta.setLore(storageLore);
        storageSlot.setItemMeta(storageMeta);

        ItemStack unusable = new ItemStack(Material.PURPLE_STAINED_GLASS);
        ItemMeta umeta = unusable.getItemMeta();
        umeta.setDisplayName(ChatColor.DARK_RED + "Unusable Slot");
        unusable.setItemMeta(umeta);

        //fill inventory based on wands

        switch (size) {
            case 9:
                for (int i = 0; i < 9; i++) {
                    if (i >= 3 && i <= 5)
                        inv.setItem(i, leftSlot);
                    else inv.setItem(i, unusable);
                }

                break;
            case 18:
                for (int i = 0; i < 18; i++) {
                    if (i == 4)
                        inv.setItem(i, combineSlot);
                    else if (i >= 11 && i <= 15)
                        inv.setItem(i, leftSlot);
                    else inv.setItem(i, unusable);
                }

                break;
            case 27:
                for (int i = 0; i < 27; i++) {
                    if (i == 4)
                        inv.setItem(i, combineSlot);
                    else if (i >= 11 && i <= 15)
                        inv.setItem(i, leftSlot);
                    else if (i >= 20 && i <= 24)
                        inv.setItem(i, rightSlot);
                    else inv.setItem(i, unusable);
                }

                break;
            case 36:
                for (int i = 0; i < 27; i++) {
                    if (i == 4)
                        inv.setItem(i, combineSlot);
                    else if (i >= 11 && i <= 15)
                        inv.setItem(i, leftSlot);
                    else if (i >= 20 && i <= 24)
                        inv.setItem(i, rightSlot);
                    else inv.setItem(i, unusable);
                }

                for (int i = 27; i < 36; i ++) {
                    inv.setItem(i, storageSlot);
                }

                break;
            case 45:
                for (int i = 0; i < 27; i++) {
                    if (i == 4)
                        inv.setItem(i, combineSlot);
                    else if (i >= 11 && i <= 15)
                        inv.setItem(i, leftSlot);
                    else if (i >= 20 && i <= 24)
                        inv.setItem(i, rightSlot);
                    else inv.setItem(i, unusable);
                }

                for (int i = 27; i < 45; i ++) {
                    inv.setItem(i, storageSlot);
                }

                break;


        }

        return inv;
    }
    public static List<Spell> getSpells(UUID id, Rarity rarity, Player player) {
        List<Spell> spells = new ArrayList<>();

        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());

        ConfigurationSection contentsSection = config.getConfigurationSection("contents");
        List<Integer> validSlots = (rarity.equals(Rarity.BASIC) || rarity.equals(Rarity.CURSED)) ? Arrays.asList(3, 4, 5) : Arrays.asList(11, 12, 13, 14, 15);

        if (contentsSection.getKeys(false).size() == 1 && contentsSection.getKeys(false).iterator().next().equals("placeholder")) {
            player.sendMessage(ChatColor.RED + "You currently have no left click spells equipped!");
        } else {
            for (String key : contentsSection.getKeys(false)) {
                try {
                    ConfigurationSection entrySection = contentsSection.getConfigurationSection(key);
                    String fields = entrySection.getString("fields");
                    String[] fieldsParts = fields.split("##");
                    int slot = Integer.parseInt(fieldsParts[fieldsParts.length - 2]); // Slot is second to last element
                    if (validSlots.contains(slot)) {
                        UUID spellId = UUID.fromString(key);
                        Spell spell = Spell.deserialize(id, spellId);
                        spells.add(spell);
                    }
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "An error occurred when trying to equip your spells.");
                }
            }
        }

        return spells;
    }
    public static List<Spell> getRightSpells(UUID id, Rarity rarity, Player player, int thing) {
        List<Spell> spells = new ArrayList<>();
        if (rarity.equals(Rarity.BASIC) || rarity.equals(Rarity.CURSED)) {
            if (thing == 0) {
                player.sendMessage(ChatColor.RED + "This wand cannot use right click spells!");
            }
            return spells;
        }
        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());

        ConfigurationSection contentsSection = config.getConfigurationSection("contents");
        List<Integer> validSlots = Arrays.asList(20, 21, 22, 23, 24);

        if (contentsSection.getKeys(false).size() == 1 && contentsSection.getKeys(false).iterator().next().equals("placeholder")) {
            player.sendMessage(ChatColor.RED + "You currently have no right click spells equipped!");
        } else {
            for (String key : contentsSection.getKeys(false)) {
                try {
                    ConfigurationSection entrySection = contentsSection.getConfigurationSection(key);
                    String fields = entrySection.getString("fields");
                    String[] fieldsParts = fields.split("##");
                    int slot = Integer.parseInt(fieldsParts[fieldsParts.length - 2]); // Slot is second to last element
                    if (validSlots.contains(slot)) {
                        UUID spellId = UUID.fromString(key);
                        Spell spell = Spell.deserialize(id, spellId);
                        spells.add(spell);
                    }
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "An error occurred when trying to equip your spells.");
                }
            }
        }

        return spells;
    }

    public static Spell getCombinationSpell(UUID id, Rarity rarity, Player player, int thing) {
        Spell combinationSpell = null;

        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());
        ConfigurationSection contentsSection = config.getConfigurationSection("contents");
        int combinationSpellSlot = (!rarity.equals(Rarity.CURSED) && !rarity.equals(Rarity.BASIC)) ? 4 : -1;

        if (combinationSpellSlot == -1 || (contentsSection.getKeys(false).size() == 1 && contentsSection.getKeys(false).iterator().next().equals("placeholder"))) {
            if (thing == 0)
                player.sendMessage(ChatColor.RED + "You have no combination spells equipped!");
        } else {
            for (String key : contentsSection.getKeys(false)) {
                try {
                    ConfigurationSection entrySection = contentsSection.getConfigurationSection(key);
                    String fields = entrySection.getString("fields");
                    String[] fieldsParts = fields.split("##");
                    int slot = Integer.parseInt(fieldsParts[fieldsParts.length - 2]); // Slot is second to last element
                    if (slot == combinationSpellSlot) {
                        UUID spellId = UUID.fromString(key);
                        combinationSpell = Spell.deserialize(id, spellId);
                        break;
                    }
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "An error occurred when trying to equip your combination spell.");
                }
            }
        }

        return combinationSpell;
    }

    private static void checkSpells(UUID id, Rarity rarity, Player p) {
        File configFile = new File("plugins" + File.separator + "Heart Of Ender" + File.separator + "Wands" + File.separator + id.toString() + ".yml");
        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());
        if (getSpells(id, rarity, p).isEmpty()) {
            config.set("currentSpell", "placeholder");
        }
        if (getRightSpells(id, rarity, p, 1).isEmpty()) {
            config.set("currentRightSpell", "placeholder");
        }
        if (getCombinationSpell(id, rarity, p, 1) == null) {
            config.set("comboSpell", "placeholder");
        }
        try {
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveInventory(Inventory inventory, UUID id, Rarity rarity, Player p) {
        File configFile = new File("plugins" + File.separator + "Heart Of Ender" + File.separator + "Wands" + File.separator + id.toString() + ".yml");
        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());
        config.set("contents", "placeholder");
        try {
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (ItemStack item: inventory) {
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();

                NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "Slot");
                if (container.has(key2, PersistentDataType.INTEGER)) {
                    try {
                        Spell spell = Spell.convertItemToSpell(item);
                        spell.serialize(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        WandInventory.checkSpells(id, rarity, p);
    }


    public static boolean isFillerSlot(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        String name = meta.getDisplayName();

        return !name.equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Spell Combine Slot") && !name.equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Left Click Spell") && !name.equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Right Click Spell") && !item.getType().equals(Material.ENCHANTED_BOOK);
    }
    public static boolean isSpellSlot(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        String name = meta.getDisplayName();

        return name.equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Spell Combine Slot") || name.equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Left Click Spell") || name.equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Right Click Spell");
    }
    public Inventory getInventory() {
        return inventory;
    }
}