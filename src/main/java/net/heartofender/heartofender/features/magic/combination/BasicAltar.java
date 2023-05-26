package net.heartofender.heartofender.features.magic.combination;


import net.heartofender.heartofender.features.magic.spells.Spell;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BasicAltar extends Altar {

    public BasicAltar() {
        super();

        // Initialize spell slots
        ItemStack spellSlot = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta spellMeta = spellSlot.getItemMeta();
        spellMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Spell");
        spellSlot.setItemMeta(spellMeta);

        // Set spell slots
        this.inventory[21] = spellSlot;
        this.inventory[23] = spellSlot;
    }

    @Override
    public boolean canCombine(List<Spell> spells) {
        if (spells.size() != 2) {
            return false;
        }

        Spell comboSpell = ComboUtils.getComboSpell(spells.get(0), spells.get(1));
        return comboSpell != null;
    }

    @Override
    public void combine(boolean success, Player p) {



    }
}