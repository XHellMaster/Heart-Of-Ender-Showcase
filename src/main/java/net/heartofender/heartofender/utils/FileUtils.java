package net.heartofender.heartofender.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileUtils {

    private static void populatePlayerDefaults(File file) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("mana.max", 150);
            config.set("mana.current", 150);

            config.set("health.max", 50);
            config.set("health.current", 50);

            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean createConfig(String category, String name) {
        new File("plugins" + File.separator + "Heart Of Ender" + File.separator + category).mkdirs();
        boolean success = false;
        try {
            File file = new File("plugins" + File.separator + "Heart Of Ender" + File.separator + category + File.separator + name + ".yml");
            if(!file.exists()) {
                success = file.createNewFile();

                if (category.equals("Player")) {
                    populatePlayerDefaults(file);
                }
            }
            else {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public static YamlConfiguration getConfig(String category, String name) {
        createConfig(category, name); // create if it doesn't exist  - if it exists it'll return a success regardless
        File file = new File("plugins" + File.separator + "Heart Of Ender" + File.separator + category + File.separator + name + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }


}
