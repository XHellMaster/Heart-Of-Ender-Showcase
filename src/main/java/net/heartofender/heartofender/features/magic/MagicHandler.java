package net.heartofender.heartofender.features.magic;

import com.jeff_media.customblockdata.CustomBlockData;
import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.combination.Altar;
import net.heartofender.heartofender.features.magic.combination.BasicAltar;
import net.heartofender.heartofender.features.magic.combination.ComboUtils;
import net.heartofender.heartofender.features.magic.combination.EnhancedAltar;
import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.features.magic.wands.Cast;
import net.heartofender.heartofender.features.magic.wands.Wand;
import net.heartofender.heartofender.features.magic.wands.WandInventory;
import net.heartofender.heartofender.player.CustomPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MagicHandler implements Listener {

    @EventHandler
    public void onInventoryClickSpell(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack cursorItem = event.getCursor();
        Player player = (Player) event.getWhoClicked();

        if (clickedInventory instanceof PlayerInventory) {
            return;
        }

        Inventory inventory = event.getInventory();
        if (inventory.getViewers().get(0).getOpenInventory().getTitle().equals("Wand Spells")) {
            int clickedSlot = event.getSlot();
            ItemStack clickedItem = inventory.getItem(clickedSlot);
            if (WandInventory.isFillerSlot(clickedItem)) {
                event.setCancelled(true);
            }
            if (cursorItem != null && cursorItem.getType().equals(Material.ENCHANTED_BOOK) && cursorItem.hasItemMeta() && cursorItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "SpellName"))) {
                if (WandInventory.isFillerSlot(clickedItem) || (clickedItem != null && clickedItem.hasItemMeta() && Spell.isSpell(clickedItem))) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You can't put a spell there!");
                } else {
                    event.setCancelled(true);

                    ItemStack spellItem = cursorItem.clone();
                    ItemMeta meta = spellItem.getItemMeta();
                    meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "Slot"), PersistentDataType.INTEGER, clickedSlot);
                    spellItem.setItemMeta(meta);

                    inventory.setItem(clickedSlot, spellItem);
                    player.setItemOnCursor(null);
                }
            } else if (cursorItem == null && clickedItem != null && clickedItem.hasItemMeta() && Spell.isSpell(clickedItem)) {
                event.setCancelled(true);

                ItemStack spellItem = clickedItem.clone();
                inventory.setItem(clickedSlot, null);
                player.setItemOnCursor(spellItem);
            } else if (cursorItem == null || WandInventory.isSpellSlot(clickedItem) || (cursorItem != null && !cursorItem.hasItemMeta() && !Spell.isSpell(cursorItem))) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can only put spells in here!");
            }
        }
    }

    @EventHandler
    public void onInventoryExit(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        Player p = (Player) e.getPlayer();
        if (inventory.getViewers().get(0).getOpenInventory().getTitle().equals("Wand Spells")) {
            ItemStack item = e.getPlayer().getItemInHand();
            ItemMeta meta = item.getItemMeta();

            PersistentDataContainer container = meta.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(Main.getInstance(), "wandID");
            if (container.has(key, PersistentDataType.STRING)) {
                UUID id = UUID.fromString(container.get(key, PersistentDataType.STRING));
                WandInventory.saveInventory(inventory, id, Wand.getWandFromItem(id, item).getRarity(), p);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getItemInHand();
        CustomPlayer p = new CustomPlayer(e.getPlayer());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "wandID");
        if (container.has(key, PersistentDataType.STRING)) {
            UUID id = UUID.fromString(container.get(key, PersistentDataType.STRING));
            Wand w = Wand.getWandFromItem(id, item);
            Action action = e.getAction();
            if (action.isRightClick()) {
                if (!p.getPlayer().isSneaking())
                    w.cast(p, Cast.RIGHT);
                else w.cycleRightSpell(p.getPlayer());
            }
            if (action.isLeftClick()) {
                if (!p.getPlayer().isSneaking()) {
                    w.cast(p, Cast.LEFT);
                } else w.cycleSpell(p.getPlayer());
            }
        }
    }

    @EventHandler
    public void onThrow(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        CustomPlayer p = new CustomPlayer(e.getPlayer());

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "wandID");
        if (container.has(key, PersistentDataType.STRING)) {
            e.setCancelled(true);
            UUID id = UUID.fromString(container.get(key, PersistentDataType.STRING));
            Wand w = Wand.getWandFromItem(id, item);
            if (p.getPlayer().isSneaking()) {
                w.open(p.getPlayer());
            } else {
                w.cast(p, Cast.COMBO);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAltar(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            Block block = event.getClickedBlock();
            if (!(new CustomBlockData(block, Main.getInstance()).has(new NamespacedKey(Main.getInstance(), "isAltar"))))
                return;
            event.setCancelled(true);
            PersistentDataContainer data = new CustomBlockData(block, Main.getInstance());
            String altarType = data.get(new NamespacedKey(Main.getInstance(), "altar"), PersistentDataType.STRING);
            Altar altar;
            String type;
            if (altarType.equals("enhanced")) {
                altar = new EnhancedAltar();
                type = "Enhanced";
            } else {
                altar = new BasicAltar();
                type = "Basic";
            }

            Inventory i = Bukkit.createInventory(p, 45, type + " Altar");

            i.setContents(altar.getInventory());

            p.openInventory(i);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        if (Altar.isAltar(item)) {
            String altarType = Altar.getAltarType(item);
            PersistentDataContainer container = new CustomBlockData(block, Main.getInstance());
            container.set(new NamespacedKey(Main.getInstance(), "isAltar"), PersistentDataType.BYTE, (byte) 1);
            container.set(new NamespacedKey(Main.getInstance(), "altar"), PersistentDataType.STRING, altarType);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        PersistentDataContainer data = new CustomBlockData(block, Main.getInstance());
        if (data.has(new NamespacedKey(Main.getInstance(), "isAltar"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        String title = inventory.getViewers().get(0).getOpenInventory().getTitle();
        int slot = event.getSlot();

        if (title.equals("Basic Altar") || title.equals("Enhanced Altar")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            if (slot == 40) {
                if (((title.equals("Basic Altar") && Spell.isSpell(inventory.getItem(21)) && Spell.isSpell(inventory.getItem(23))))) {
                    try {
                        Spell a = Spell.convertItemToSpell(inventory.getItem(21));
                        Spell b = Spell.convertItemToSpell(inventory.getItem(23));
                        Spell res = ComboUtils.getComboSpell(a, b);
                        Block block = player.getTargetBlock(null, 5);
                        if (Altar.isAltar(block)) {
                            Altar altar = new BasicAltar();
                            inventory.setItem(21, null);
                            inventory.setItem(23, null);
                            if (res != null) {
                                altar.combine(true, player);
                            } else {
                                altar.combine(false, player);
                            }
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "You look in awe at the altar, something is happening!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (title.equals("Enhanced Altar") && Spell.isSpell(inventory.getItem(21)) && Spell.isSpell(inventory.getItem(22)) && Spell.isSpell(inventory.getItem(23))) {
                    try {
                        Spell a = Spell.convertItemToSpell(inventory.getItem(21));
                        Spell b = Spell.convertItemToSpell(inventory.getItem(22));
                        Spell c = Spell.convertItemToSpell(inventory.getItem(23));
                        Spell res = ComboUtils.getEnhancedComboSpell(a, b, c);
                        Block block = player.getTargetBlock(null, 5);
                        if (Altar.isAltar(block)) {
                            Altar altar = new EnhancedAltar();
                            // Update the inventory
                            inventory.setItem(21, null);
                            inventory.setItem(22, null);
                            inventory.setItem(23, null);
                            if (res != null) {
                                altar.combine(true, player);
                            } else {
                                altar.combine(false, player);
                            }
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "You look in awe at the altar, something is happening!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (slot == 21 || slot == 22 || slot == 23) {
                // Allow the player to place spells in these slots
                ItemStack pitem = event.getCursor();
                if (Spell.isSpell(pitem) || (pitem == null && event.getCurrentItem() != null && Spell.isSpell(event.getCurrentItem()))) {
                    event.setCancelled(false);
                }
                else {
                    player.sendMessage(ChatColor.DARK_RED + "You cannot that item in here...");
                }


            }
        }
    }

}