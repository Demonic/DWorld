package net.Demonly.dworld.manager;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Worlds
{

    private List<World> loadedWorlds = new ArrayList<World>();
    private List<World> unloadedWorlds = new ArrayList<World>();

    public List<Player> getWorldPlayers(World world)
    {
        return world.getPlayers();
    }

    public List<World> getLoadedWorlds()
    {
        return loadedWorlds;
    }

    public List<World> getUnloadedWorlds()
    {
        return unloadedWorlds;
    }

}
