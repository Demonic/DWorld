package net.Demonly.dworld;

import net.Demonly.dworld.config.Configuration;
import net.Demonly.dworld.manager.WorldManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;;

public class DWorld extends JavaPlugin
{
    private Logger logger = LoggerFactory.getLogger("DWorld");
    private FileConfiguration worldsConfig;
    private FileConfiguration baseConfig;
    private WorldManager worldManager;

    @Override
    public void onEnable()
    {
        // Logging
        logger.info("DWorld creating all worlds in 7 days. This may take awhile.");
        loadConfigs();

        // Worlds
        setupWorldManager();
        worldManager.loadWorlds();
    }

    @Override
    public void onDisable()
    {
        logger.info("DWorld is unloading the world");

        // unload
        worldManager.unloadWorlds();
    }

    public FileConfiguration getWorldsConfig() {
        return worldsConfig;
    }

    public FileConfiguration getBaseConfig() {
        return baseConfig;
    }

    public Logger log() {
        return logger;
    }

    private void loadConfigs() {
        worldsConfig = new Configuration(new File(getDataFolder(), "worlds.yml"), this).getConfig();
        baseConfig = new Configuration(new File(getDataFolder(), "config.yml"), this).getConfig();
    }

    private void setupWorldManager() {
        this.worldManager = new WorldManager(worldsConfig, this);
    }

}
