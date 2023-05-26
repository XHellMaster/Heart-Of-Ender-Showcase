package net.heartofender.heartofender.features.magic.combination;

import net.heartofender.heartofender.features.magic.spells.Spell;
import net.heartofender.heartofender.features.magic.spells.combinations.Typhoon;
import net.heartofender.heartofender.features.magic.spells.damage.GaleForce;
import net.heartofender.heartofender.features.magic.spells.damage.Whirlpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboUtils {

    private static Map<Spell, List<Spell>> combos = new HashMap<>();

    //Fire + Air = "Inferno"
    //Fire + Earth = "Lava"
    //Fire + Light = "Radiance"
    //Fire + Darkness = "Ember"
    //Water + Air = "Vortex"
    //Water + Light = "Glimmer"
    //Water + Darkness = "Abyss"
    //Air + Light = "Zephyr"
    //Air + Darkness = "Void"
    //Earth + Light = "Crystalline"
    //Earth + Darkness = "Obsidian"

    public static void addSpells() {
        ArrayList<Spell> combo1 = new ArrayList<Spell>();
        combo1.add(new GaleForce());
        combo1.add(new Whirlpool());
        combos.put(new Typhoon(), combo1);
    }

    public static Spell getComboSpell(Spell a, Spell b) {
        for (Map.Entry<Spell, List<Spell>> entry : combos.entrySet()) {
            if (entry.getValue().contains(a) && entry.getValue().contains(b)) {
                return entry.getKey();
            }
        }
        return null;
    }
    public static Spell getEnhancedComboSpell(Spell a, Spell b, Spell c) {
        for (Map.Entry<Spell, List<Spell>> entry : combos.entrySet()) {
            if (entry.getValue().contains(a) && entry.getValue().contains(b) && entry.getValue().contains(c)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
