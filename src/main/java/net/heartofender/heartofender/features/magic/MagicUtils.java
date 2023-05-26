package net.heartofender.heartofender.features.magic;

import net.heartofender.heartofender.features.magic.elements.Element;
import net.heartofender.heartofender.features.magic.rarity.Rarity;

public class MagicUtils {

    public static String getElementColor(Element e) {
        String elementColor = "";
        switch (e) {
            case AIR:
                elementColor = "§b";
                break;
            case DARKNESS:
                elementColor = "§8";
                break;
            case EARTH:
                elementColor = "§a";
                break;
            case FIRE:
                elementColor = "§c";
                break;
            case LIGHT:
                elementColor = "§l§f";
                break;
            case WATER:
                elementColor = "§9";
                break;
            case INFERNO:
            case LAVA:
            case RADIANCE:
            case EMBER:
            case VORTEX:
            case GLIMMER:
            case ABYSS:
            case ZEPHYR:
            case VOID:
            case CRYSTALLINE:
            case OBSIDIAN:
                elementColor = "§6";
                break;
            default:
                elementColor = "";
                break;
        }
        return elementColor;
    }

    public static String getElementName(Element e) {
        String name = "";
        switch (e) {
            case AIR:
                name = "§bAir";
                break;
            case DARKNESS:
                name = "§8Darkness";
                break;
            case EARTH:
                name = "§aEarth";
                break;
            case FIRE:
                name = "§cFire";
                break;
            case LIGHT:
                name = "§l§fLight";
                break;
            case WATER:
                name = "§9Water";
                break;
            case INFERNO:
                name = "§6Inferno";
                break;
            case LAVA:
                name = "§6Lava";
                break;
            case RADIANCE:
                name = "§6Radiance";
                break;
            case EMBER:
                name = "§6Ember";
                break;
            case VORTEX:
                name = "§6Vortex";
                break;
            case GLIMMER:
                name = "§6Glimmer";
                break;
            case ABYSS:
                name = "§6Abyss";
                break;
            case ZEPHYR:
                name = "§6Zephyr";
                break;
            case VOID:
                name = "§6Void";
                break;
            case CRYSTALLINE:
                name = "§6Crystalline";
                break;
            case OBSIDIAN:
                name = "§6Obsidian";
                break;
            case NONE:
                name = "§6§lNONE!";
                break;
            case ALL:
                name = "§6§lALL!";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }

    public static String getRarityColor(Rarity r) {
        String rarity = "";
        switch (r) {
            case CURSED: {
                rarity = "§4";
                break;
            }
            case BASIC: {
                rarity = "§f";
                break;
            }
            case ENCHANTED: {
                rarity = "§9";
                break;
            }
            case GRAND: {
                rarity = "§b";
                break;
            }
            case ETHEREAL: {
                rarity = "§5";
                break;
            }
            case CELESTIAL: {
                rarity = "§d";
                break;
            }

            default: {
                rarity = "";
                break;
            }
        }
        return rarity;
    }

    public static String getRarityName(Rarity r) {
        String rarityName = "";
        switch (r) {
            case CURSED: {
                rarityName = "§4§lCURSED";
                break;
            }
            case BASIC: {
                rarityName = "§f§lBASIC";
                break;
            }
            case ENCHANTED: {
                rarityName = "§9§lENCHANTED";
                break;
            }
            case GRAND: {
                rarityName = "§b§lGRAND";
                break;
            }
            case ETHEREAL: {
                rarityName = "§5§lETHEREAL";
                break;
            }
            case CELESTIAL: {
                rarityName = "§d§lCELESTIAL";
                break;
            }
            default: {
                rarityName = "";
                break;
            }
        }
        return rarityName;
    }


}
