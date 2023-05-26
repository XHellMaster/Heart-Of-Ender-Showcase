package net.heartofender.heartofender.features.magic.combination;

import net.heartofender.heartofender.features.magic.spells.Spell;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EnhancedAltar extends Altar {

    public EnhancedAltar() {
        super();

        // Initialize spell slots
        ItemStack spellSlot = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta spellMeta = spellSlot.getItemMeta();
        spellMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Spell");
        spellSlot.setItemMeta(spellMeta);

        // Set spell slots
        this.inventory[21] = spellSlot;
        this.inventory[22] = spellSlot;
        this.inventory[23] = spellSlot;
    }

    @Override
    public boolean canCombine(List<Spell> spells) {
        if (spells.size() != 3) {
            return false;
        }

        Spell comboSpell = ComboUtils.getEnhancedComboSpell(spells.get(0), spells.get(1), spells.get(2));
        return comboSpell != null;
    }

    @Override
    public void combine(boolean success, Player p) {

    }
}