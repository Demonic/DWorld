package net.Demonly.dworld.manager;

import net.Demonly.dworld.DWorld;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;

public class WorldManager implements Listener
{

    private FileConfiguration worlds;
    private DWorld plugin;

    public WorldManager(FileConfiguration worlds, DWorld plugin)
    {
        this.plugin = plugin;
        this.worlds = plugin.getWorldsConfig();
    }

    public void loadWorlds()
    {

        worlds.getConfigurationSection("Worlds").getKeys(false).forEach(world ->
        {
            if (worlds.getBoolean("Worlds." + world + ".enabled"))
            {
                plugin.log().info("{} loading..", world);
                loadWorld(world,
                        worlds.getString("Worlds." + world + ".generator"),
                        worlds.getString("Worlds." + world + ".environment"),
                        worlds.getBoolean("Worlds." + world + "pvp"),
                        worlds.getObject("Worlds." + world + ".spawn_location", Location.class),
                        worlds.getString("Worlds." + world + ".difficulty"),
                        worlds.getBoolean("Worlds." + world + ".allow_monsters"),
                        worlds.getBoolean("Worlds." + world + ".allow_animals"),
                        worlds.getLong("Worlds." + world + ".seed"));
                plugin.getWorlds().getLoadedWorlds().add(Bukkit.getWorld(world));
            }
        });
    }

    public void unloadWorlds()
    {
        worlds.getConfigurationSection("Worlds").getKeys(false).forEach(world ->
        {
            if (worldExists(world) && worlds.getBoolean("Worlds." + world + ".enabled"))
            {
                // attempt to unload world.
                try
                {
                    unloadWorld(plugin.getServer().getWorld(world));
                    plugin.log().info( "World unloaded.. {}", world);
                } catch (Exception e) {
                    plugin.log().error("Error occurred while unloading world.. {}", world, e);
                }
            }
        });
    }

    private void loadWorld(String name, String generator, String environment, boolean pvp, Location spawn, String difficulty, boolean allowMonsters, boolean allowAnimals, Long seed)
    {
        try
        {
            new BukkitRunnable()
            {
                public void run()
                {
                    WorldCreator worldCreator = new WorldCreator(name);
                    if (null != environment) worldCreator.environment(World.Environment.valueOf(environment));
                    if (null != generator) worldCreator.generator(generator);
                    if (null != seed) worldCreator.seed(seed);

                    World w = worldCreator.createWorld();

                    w.setPVP(pvp);
                    if (null != spawn) w.setSpawnLocation(spawn);
                    if (null != difficulty) w.setDifficulty(Difficulty.valueOf(difficulty));
                    w.setSpawnFlags(allowMonsters, allowAnimals);
                }
            }.run();
        } catch (Exception e) {
            plugin.log().error("ERROR LOADING WORLD: {} !", name, e);
        }
    }

    public void unloadWorld(World world)
    {
        try
        {
            plugin.log().info("ATTEMPTING TO UNLOAD WORLD {} !", world);
            plugin.getServer().unloadWorld(world, true);
            plugin.getWorlds().getLoadedWorlds().remove(world);
            Chunk[] chunks = world.getLoadedChunks();
            Arrays.stream(chunks).iterator().forEachRemaining(chunk ->
            {
                chunk.unload();
            });
            System.gc();

        } catch (Exception e) {
            plugin.log().error("ERROR UNLOADING WORLD {} !", world, e);
        }
    }

    private void deleteWorld(World world)
    {
        try
        {
            File worldFolder = world.getWorldFolder();
            unloadWorld(world);
            worldFolder.delete();
            System.gc();
        } catch (Exception e) {
            plugin.log().error("ERROR DELETING WORLD {} !", world, e);
        }
    }

    private boolean worldExists(String name)
    {
        if (null != plugin.getServer().getWorld(name)) {
            return true;
        }
        return false;
    }
}
