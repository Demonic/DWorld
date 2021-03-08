package net.Demonly.dworld.manager;

import net.Demonly.dworld.DWorld;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldManager implements Listener
{

    private FileConfiguration worlds;
    private DWorld plugin;
    private List<String> loadedWorlds = new ArrayList<String>();

    public WorldManager(FileConfiguration worlds, DWorld plugin)
    {
        this.plugin = plugin;
        this.worlds = plugin.getWorldsConfig();
    }

    public void loadWorlds()
    {
        List<String> list = new ArrayList<String>();

        worlds.getConfigurationSection("Worlds").getKeys(false).forEach(world ->
        {
            if (worlds.getBoolean("Worlds." + world + ".enabled"))
            {
                if (worldExists(world)) {
                    plugin.log().info("World {} exists, loading...", world);
                    loadWorld(world);
                    loadedWorlds.add(world);

                } else {
                    plugin.log().info("World {} does not exist attempting to create..", world);
                    createWorld(world);
                }
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

    public List<String> getLoadedWorlds()
    {
        return loadedWorlds;
    }

    private void createWorld(String name)
    {
        // We're eventually going to get the world details from the config here.

        try
        {
            WorldCreator worldCreator = new WorldCreator(name);
            worldCreator.environment(World.Environment.NORMAL);
            worldCreator.type(WorldType.NORMAL);
            worldCreator.createWorld();
        } catch (Exception e) {
            plugin.log().error("ERROR CREATING WORLD: {} !", name, e);
        }
    }

    private void loadWorld(String name)
    {
        try
        {
            WorldCreator worldCreator = new WorldCreator(name);
            worldCreator.environment(World.Environment.NORMAL);
            worldCreator.type(WorldType.NORMAL);
            worldCreator.createWorld();
        } catch (Exception e) {
            plugin.log().error("ERROR LOADING WORLD: {} !", name, e);
        }
    }

    private void unloadWorld(World world)
    {
        try
        {
            plugin.getServer().unloadWorld(world, true);
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
