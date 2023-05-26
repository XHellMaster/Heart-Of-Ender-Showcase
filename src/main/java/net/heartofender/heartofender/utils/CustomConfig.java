package net.heartofender.heartofender.utils;

import net.heartofender.heartofender.utils.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CustomConfig {

    File file;
    String category;
    String name;
    YamlConfiguration config;


    public CustomConfig(String category, String name) {
        this.file = new File("plugins" + File.separator + "Heart Of Ender" + File.separator + category + File.separator + name + ".yml");
        this.category = category;
        this.name = name;
        this.config = FileUtils.getConfig(this.category, this.name);
    }


    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ops

    public void set(String key, Object value) {
        config.set(key, value);
    }

    public Object get(String key) {
        return config.get(key);
    }


    // gets

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public File getFile() {
        return this.file;
    }

}
