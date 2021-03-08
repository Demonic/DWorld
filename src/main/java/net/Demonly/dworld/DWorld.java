package net.Demonly.dworld;

import net.Demonly.dworld.config.Configuration;
import net.Demonly.dworld.manager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;;

public class DWorld extends JavaPlugin
{
    private Logger logger = LoggerFactory.getLogger("DWorld");
    private Configuration worldsConfig;
    private Configuration baseConfig;
    private WorldManager worldManager;

    @Override
    public void onEnable()
    {
        // Logging
        logger.info("DWorld creating all worlds in 7 days. This may take awhile.");
        loadConfigs();

        // Add default worlds
        addDefaultWorlds();

        // Worlds
        setupWorldManager();
        worldManager.loadWorlds();
    }

    @Override
    public void onDisable()
    {
        logger.info("DWorld is unloading the world");
    }

    public FileConfiguration getWorldsConfig()
    {
        return worldsConfig.getConfig();
    }

    public FileConfiguration getBaseConfig()
    {
        return baseConfig.getConfig();
    }

    // slf4j
    public Logger log()
    {
        return logger;
    }

    private void loadConfigs()
    {
        worldsConfig = new Configuration(new File(getDataFolder(), "worlds.yml"), this);
        baseConfig = new Configuration(new File(getDataFolder(), "config.yml"), this);
    }

    private void setupWorldManager()
    {
        this.worldManager = new WorldManager(worldsConfig.getConfig(), this);
    }

    private void addDefaultWorlds()
    {
        Bukkit.getWorlds().forEach(world ->
        {
            if (!worldsConfig.getConfig().contains("Worlds."+world.getName()))
            {
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".enabled", true);
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".difficulty", world.getDifficulty().name());
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".pvp_enabled", world.getPVP());
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".spawn_location", world.getSpawnLocation().toString());
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".type", world.getEnvironment().name());
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".allow_monsters", world.getAllowMonsters());
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".allow_animals", world.getAllowAnimals());
                worldsConfig.getConfig().set("Worlds." + world.getName() + ".generator", world.getGenerator());
            }
        });
        worldsConfig.saveConfig();
    }

}