package net.heartofender.heartofender;

import net.heartofender.heartofender.commands.Test;

import net.heartofender.heartofender.features.magic.MagicHandler;
import net.heartofender.heartofender.features.magic.spells.SpellListener;
import net.heartofender.heartofender.player.CustomPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    private static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getPluginManager().registerEvents(new CustomPlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new MagicHandler(), this);
        Bukkit.getPluginManager().registerEvents(new SpellListener(), this);

        Test test = new Test();
        this.getCommand("test").setExecutor(test);
        this.getCommand("tidalwave").setExecutor(test);
        this.getCommand("whirlpool").setExecutor(test);
        this.getCommand("blazefury").setExecutor(test);
        this.getCommand("galeforce").setExecutor(test);
        this.getCommand("typhoon").setExecutor(test);
        this.getCommand("altar").setExecutor(test);
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return INSTANCE;
    }

}
