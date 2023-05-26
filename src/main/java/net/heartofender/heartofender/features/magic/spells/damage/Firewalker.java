package net.heartofender.heartofender.features.magic.spells.damage;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.spells.SClassifier;
import net.heartofender.heartofender.features.magic.spells.Spell;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class Firewalker extends Spell {

    public Firewalker() {
        super(Element.FIRE, Rarity.BASIC, 20, "Firewalker", "Gives the player temporary immunity to fire and lava.", SClassifier.UTILITY, "net.heartofender.heartofender.features.magic.spells.damage.Firewalker", 22);
    }

    public Firewalker(Element e, Rarity r, double cost, String name, SClassifier type, ItemStack item, String classifier, int slot, UUID id, double otherParam) {
        super(e, r, cost, name, type, item, classifier, slot, id, otherParam);
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0));
    }
}
