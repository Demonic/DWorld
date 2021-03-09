package net.Demonly.dworld.menu;

import net.Demonly.dworld.DWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class WorldsMenu implements Listener
{

    private final Inventory inventory;
    private DWorld plugin;

    public WorldsMenu(DWorld plugin)
    {
        // init plugin
        this.plugin = plugin;

        // init inventory
        inventory = Bukkit.createInventory(null, 54, "Worlds Menu");

        // init items
        initItems();
    }

    public void initItems()
    {
        // Put items in the inventory
        plugin.getWorlds().getLoadedWorlds().forEach(world ->
        {
            plugin.log().info("Adding in world to menu.. {}", world.getName());
            inventory.addItem(createItem(Material.DIRT, world.getName(), "Left Click to teleport", "Right Click to unload world"));
        });
    }

    protected ItemStack createItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity player)
    {
     player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent ev)
    {
        if (ev.getInventory() != inventory)
        {
            return;
        }

        ev.setCancelled(true);

        // teleport player to world when clicked.
        if (ev.getCurrentItem() != null && ev.getClick() == ClickType.LEFT)
        {
            plugin.log().info("Player left clicked world.. {}", ev.getCurrentItem().getItemMeta().getDisplayName());
            ev.getWhoClicked().teleport(new Location(Bukkit.getWorld(ev.getCurrentItem().getItemMeta().getDisplayName()), 90.0 , 90.0, 90.0));
        }

        if (ev.getCurrentItem() != null && ev.getClick() == ClickType.RIGHT)
        {
            plugin.log().info("Player right clicked world.. {}", ev.getCurrentItem().getItemMeta().getDisplayName());
            plugin.getWorldManager().unloadWorld(Bukkit.getWorld(ev.getCurrentItem().getItemMeta().getDisplayName()));
            ev.getWhoClicked().closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent ev)
    {
        if (ev.getInventory() != inventory)
        {
            return;
        }

        ev.setCancelled(true);
    }

}
