package net.Demonly.dworld.listeners;

import net.Demonly.dworld.DWorld;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobSpawnListener implements Listener
{
    private DWorld plugin;
    public MobSpawnListener(DWorld plugin) {
        this.plugin = plugin;
    }

    public void onMobSpawn(EntitySpawnEvent ev)
    {
        World world = ev.getLocation().getWorld();

        // Stop animal spawning if the world does not support it.
        if (ev.getEntity() instanceof Animals)
        {
            if (!plugin.getWorldsConfig().getBoolean("Worlds." + world.getName() + ".allow_animals"))
            {
                ev.setCancelled(true);
                return;
            }
        }

        // Stop monster spawning if the world does not support it/
        if (ev.getEntity() instanceof Monster)
        {
            if (!plugin.getWorldsConfig().getBoolean("Worlds." + world.getName() + ".allow_monsters"))
            {
                ev.setCancelled(true);
                return;
            }
        }

        plugin.log().info("Entity is spawning.. {}" + ev.getEntity().getType());

    }

}
