package net.heartofender.heartofender.features.magic.spells;

import net.heartofender.heartofender.Main;
import net.heartofender.heartofender.features.magic.MagicUtils;
import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;
import net.heartofender.heartofender.features.magic.wands.Cast;
import net.heartofender.heartofender.utils.FileUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.heartofender.heartofender.features.magic.MagicUtils.*;

public abstract class Spell {

    public double manaCost;
    public Element element;
    public Rarity rarity;

    private ItemStack spellItem;
    private String name;

    private SClassifier type;
    private UUID spellID; //used to avoid issues when saving spells, don't worry about this
    private String classifier; //ditto here, no touchy

    protected double otherParam;
    private int slot = 0;

    public double calculateDamage(Element e, Element e2, double spellPower, double wandAffinity, Rarity r /*The rarity of the wand */) {
        int isStrong = (element == e) ? 1 : (element == e2) ? -1 : 0; /* -1 for weak, 1 for strong, 0 for neither */
        double elementModifier = (isStrong == 1) ? 1.2 : (isStrong == -1) ? 0.8 : 1;
        double spellModifier = (spellPower + 100)/100;
        double wandModifier = (wandAffinity + 100)/100;
        double rarityModifier = r.equals(Rarity.CELESTIAL) ? 1.5: r.equals(Rarity.CURSED) ? 0.5: 1;
        return wandModifier * rarityModifier * spellModifier * elementModifier * otherParam;
    }

    public Spell(Element e, Rarity r, double cost, String name, String description, SClassifier s, String classifier, double otherParam) { // new spells
        element = e;
        rarity = r;
        manaCost = cost;
        this.name = name;
        type = s;
        this.classifier = classifier;
        spellItem = new ItemStack(Material.ENCHANTED_BOOK);
        spellID = UUID.randomUUID();
        this.otherParam = otherParam;
        ItemMeta spellMeta = spellItem.getItemMeta();
        spellMeta.setUnbreakable(true);
        List<String> lore = new ArrayList<>();
        String nameColor = getElementColor(element);
        spellMeta.setDisplayName(nameColor + name);
        String elementStrong = "§6Element: " + getElementName(element);
        String costMana = "§dMana Cost: " + manaCost ;
        String rar = getRarityName(r);
        lore.add(elementStrong);
        lore.add(costMana);
        lore.add(nameColor + description);
        lore.add(rar);
        spellMeta.setLore(lore);
        spellItem.setItemMeta(spellMeta);
        //set NBT data

        ItemMeta meta = spellItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey keyName = new NamespacedKey(Main.getInstance(), "SpellName");
        NamespacedKey keyElement = new NamespacedKey(Main.getInstance(), "Element");
        NamespacedKey keyRarity = new NamespacedKey(Main.getInstance(), "Rarity");
        NamespacedKey keyManaCost = new NamespacedKey(Main.getInstance(), "ManaCost");
        NamespacedKey keyType = new NamespacedKey(Main.getInstance(), "Type");
        NamespacedKey keyClassifier = new NamespacedKey(Main.getInstance(), "Classifier");
        NamespacedKey keySlot = new NamespacedKey(Main.getInstance(), "Slot");
        NamespacedKey keyID = new NamespacedKey(Main.getInstance(), "ID");
        NamespacedKey keyDamage = new NamespacedKey(Main.getInstance(), "Damage");
        NamespacedKey keyHealth = new NamespacedKey(Main.getInstance(), "Healing");
        NamespacedKey keyUtility = new NamespacedKey(Main.getInstance(), "Utility");
        container.set(keyName, PersistentDataType.STRING, this.name);
        container.set(keyElement, PersistentDataType.STRING, this.element.toString());
        container.set(keyRarity, PersistentDataType.STRING, this.rarity.toString());
        container.set(keyManaCost, PersistentDataType.DOUBLE, this.manaCost);
        container.set(keyType, PersistentDataType.STRING, this.type.toString());
        container.set(keyClassifier, PersistentDataType.STRING, this.classifier);
        container.set(keySlot, PersistentDataType.INTEGER, this.slot);
        container.set(keyID, PersistentDataType.STRING, this.spellID.toString());
        if (s.equals(SClassifier.DAMAGE)) {
            container.set(keyDamage, PersistentDataType.DOUBLE, otherParam);
        }
        else if (s.equals(SClassifier.HEAL)) {
            container.set(keyHealth, PersistentDataType.DOUBLE, otherParam);
        }
        else if (s.equals(SClassifier.UTILITY)) {
            container.set(keyUtility, PersistentDataType.DOUBLE, otherParam);
        }

        spellItem.setItemMeta(meta);
    }

    public Spell(Element e, Rarity r, double cost, String name, SClassifier s, ItemStack currentItem, String classifier, int slot, UUID id, double otherParam) { //for existing spells (framework usage only, use the other constructor for creating spells)
        element = e;
        rarity = r;
        manaCost = cost;
        this.name = name;
        type = s;
        spellItem = currentItem;
        this.classifier = classifier;
        this.slot = slot;
        spellID = id;
        this.otherParam = otherParam;
    }
    public SClassifier getType() {
        return type;
    }
    public abstract void cast(Player p);

    public String getName() {
        return name;
    }
    public String getColoredName() {
        return MagicUtils.getElementColor(element) + name;
    }
    public Element getElement() {
        return element;
    }
    public Rarity getRarity() {
        return rarity;
    }

    public double getManaCost() {
        return manaCost;
    }

    public UUID getID() {
        return spellID;
    }
    public void serialize(UUID id) throws IOException {
        File configFile = new File("plugins" + File.separator + "Heart Of Ender" + File.separator + "Wands" + File.separator + id.toString() + ".yml");
        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());
        String ser = name + "##" + element + "##" + rarity + "##" + manaCost + "##" + type + "##" + classifier + "##" + slot + "##" + otherParam;
        config.set("contents." + spellID + ".item", spellItem);
        config.set("contents." + spellID + ".fields", ser);
        config.save(configFile);
    }

    public void serializeForWand(UUID id, Cast c) throws IOException {
        File configFile = new File("plugins" + File.separator + "Heart Of Ender" + File.separator + "Wands" + File.separator + id.toString() + ".yml");
        YamlConfiguration config = FileUtils.getConfig("Wands", id.toString());
        String ser = name + "##" + element + "##" + rarity + "##" + manaCost + "##" + type + "##" + classifier + "##" + slot + "##" + otherParam;

        switch (c) {
            case LEFT -> {
                config.set("currentSpell", "placeholder");
                config.save(configFile);
                config = FileUtils.getConfig("Wands", id.toString());
                config.set("currentSpell." + spellID.toString() + ".item", spellItem);
                config.set("currentSpell." + spellID.toString() + ".fields", ser);
            }
            case RIGHT -> {
                config.set("currentRightSpell", "placeholder");
                config.save(configFile);
                config = FileUtils.getConfig("Wands", id.toString());
                config.set("currentRightSpell." + spellID.toString() + ".item", spellItem);
                config.set("currentRightSpell." + spellID.toString() + ".fields", ser);
            }
            case COMBO -> {
                config.set("comboSpell", "placeholder");
                config.save(configFile);
                config = FileUtils.getConfig("Wands", id.toString());
                config.set("comboSpell." + spellID.toString() + ".item", spellItem);
                config.set("comboSpell." + spellID.toString() + ".fields", ser);
            }
        }

        config.save(configFile);
    }
    public static Spell deserialize(UUID wandID, UUID spellID) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        YamlConfiguration config = FileUtils.getConfig("Wands", wandID.toString());
        String[] elements = config.getString("contents." + spellID + ".fields").split("##");
        Element e = Element.valueOf(elements[1]);
        Rarity r = Rarity.valueOf(elements[2]);
        String name = elements[0];
        double manaCost = Double.parseDouble(elements[3]);
        SClassifier type = SClassifier.valueOf(elements[4]);
        ItemStack item = config.getItemStack("contents." + spellID + ".item");
        String csfr = elements[5];
        int slot = Integer.parseInt(elements[6]);
        double otherParam = Double.parseDouble(elements[7]);
        Class<?> nameClass = Class.forName(csfr);

        Constructor<?> constructor = nameClass.getConstructor(Element.class, Rarity.class, double.class, String.class, SClassifier.class, ItemStack.class, String.class, int.class, UUID.class, double.class);
        Object object = constructor.newInstance(e, r, manaCost, name, type, item, csfr, slot, spellID, otherParam);
        return (Spell) object;
    }

    public static Spell deserializeForWand(UUID wandID, UUID spellID, Cast c) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        YamlConfiguration config = FileUtils.getConfig("Wands", wandID.toString());
        String[] elements = null;
        ItemStack item = null;
        switch (c) {
            case LEFT -> {
                elements = config.getString("currentSpell." + spellID + ".fields").split("##");
                item = config.getItemStack("currentSpell." + spellID + ".item");
            }
            case RIGHT -> {
                elements = config.getString("currentRightSpell." + spellID + ".fields").split("##");
                item = config.getItemStack("currentRightSpell." + spellID + ".item");
            }
            case COMBO -> {
                elements = config.getString("comboSpell." + spellID + ".fields").split("##");
                item = config.getItemStack("comboSpell." + spellID + ".item");
            }
        }

        Element e = Element.valueOf(elements[1]);
        Rarity r = Rarity.valueOf(elements[2]);
        String name = elements[0];
        double manaCost = Double.parseDouble(elements[3]);
        SClassifier type = SClassifier.valueOf(elements[4]);
        String csfr = elements[5];
        int slot = Integer.parseInt(elements[6]);
        double otherParam = Double.parseDouble(elements[7]);
        Class<?> nameClass = Class.forName(csfr);

        Constructor<?> constructor = nameClass.getConstructor(Element.class, Rarity.class, double.class, String.class, SClassifier.class, ItemStack.class, String.class, int.class, UUID.class, double.class);
        Object object = constructor.newInstance(e, r, manaCost, name, type, item, csfr, slot, spellID, otherParam);
        return (Spell) object;
    }

    public static boolean isSpell(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey keyName = new NamespacedKey(Main.getInstance(), "SpellName");

        return container.has(keyName, PersistentDataType.STRING);
    }

    public int getSlot() {
        return slot;
    }
    public void setSlot(int slot) {
        this.slot = slot;
    }
    public ItemStack getSpellItem() {
        return spellItem;
    }

    public static Spell convertItemToSpell(org.bukkit.inventory.ItemStack item) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey keyName = new NamespacedKey(Main.getInstance(), "SpellName");
        NamespacedKey keyElement = new NamespacedKey(Main.getInstance(), "Element");
        NamespacedKey keyRarity = new NamespacedKey(Main.getInstance(), "Rarity");
        NamespacedKey keyManaCost = new NamespacedKey(Main.getInstance(), "ManaCost");
        NamespacedKey keyType = new NamespacedKey(Main.getInstance(), "Type");
        NamespacedKey keyClassifier = new NamespacedKey(Main.getInstance(), "Classifier");
        NamespacedKey keySlot = new NamespacedKey(Main.getInstance(), "Slot");
        NamespacedKey keyID = new NamespacedKey(Main.getInstance(), "ID");
        NamespacedKey keyDamage = new NamespacedKey(Main.getInstance(), "Damage");
        NamespacedKey keyHealth = new NamespacedKey(Main.getInstance(), "Healing");
        NamespacedKey keyUtility = new NamespacedKey(Main.getInstance(), "Utility");
        String name = container.get(keyName, PersistentDataType.STRING);
        Element element = Element.valueOf(container.get(keyElement, PersistentDataType.STRING));
        Rarity rarity = Rarity.valueOf(container.get(keyRarity, PersistentDataType.STRING));
        double manaCost = container.get(keyManaCost, PersistentDataType.DOUBLE);
        SClassifier type = SClassifier.valueOf(container.get(keyType, PersistentDataType.STRING));
        String classifier = container.get(keyClassifier, PersistentDataType.STRING);
        int slot = container.get(keySlot, PersistentDataType.INTEGER);
        UUID spellID = UUID.fromString(container.get(keyID, PersistentDataType.STRING));
        Class<?> nameClass = Class.forName(classifier);

        if (container.has(keyDamage)) {
            double damage = container.get(keyDamage, PersistentDataType.DOUBLE);
            Constructor<?> constructor = nameClass.getConstructor(Element.class, Rarity.class, double.class, String.class, SClassifier.class, ItemStack.class, String.class, int.class, UUID.class, double.class);
            Object object = constructor.newInstance(element, rarity, manaCost, name, type, item, classifier, slot, spellID, damage);

            return (Spell) object;
        }
        else if (container.has(keyHealth)) {
            double health = container.get(keyHealth, PersistentDataType.DOUBLE);
            Constructor<?> constructor = nameClass.getConstructor(Element.class, Rarity.class, double.class, String.class, SClassifier.class, ItemStack.class, String.class, int.class, UUID.class, double.class);
            Object object = constructor.newInstance(element, rarity, manaCost, name, type, item, classifier, slot, spellID, health);

            return (Spell) object;
        }
        else if (container.has(keyUtility)) {
            double utility = container.get(keyUtility, PersistentDataType.DOUBLE);
            Constructor<?> constructor = nameClass.getConstructor(Element.class, Rarity.class, double.class, String.class, SClassifier.class, ItemStack.class, String.class, int.class, UUID.class, double.class);
            Object object = constructor.newInstance(element, rarity, manaCost, name, type, item, classifier, slot, spellID, utility);
            return (Spell) object;
        }

        return null;
    }
}
