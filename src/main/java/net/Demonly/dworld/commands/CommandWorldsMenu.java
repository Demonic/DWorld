package net.Demonly.dworld.commands;

import net.Demonly.dworld.DWorld;
import net.Demonly.dworld.menu.WorldsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class CommandWorldsMenu implements CommandExecutor
{

    private DWorld plugin;

    public CommandWorldsMenu(DWorld plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            plugin.getWorldsMenu().openInventory((HumanEntity) sender);
        }

        return true;
    }
}
