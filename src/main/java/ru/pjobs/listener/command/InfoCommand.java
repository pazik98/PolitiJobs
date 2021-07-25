package ru.pjobs.listener.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.worker.Player;
import ru.pjobs.worker.PlayerManager;

public class InfoCommand implements CommandExecutor {

    private PolitiJobsMain plugin;

    public InfoCommand(PolitiJobsMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("pjobs.info")) {
            sender.sendMessage(ChatColor.RED + "You dont have permission!");
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof org.bukkit.entity.Player)) {
                sender.sendMessage(ChatColor.RED + "Can't use in console!");
                return true;
            }
            Player p = PlayerManager.playerContainer.getPlayerByName(sender.getName());
            sender.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.WHITE + " info:\n Profession: " + p.getProfession().getName());
            return true;
        }
        else if (args.length == 1) {
            Player p = PlayerManager.playerContainer.getPlayerByName(args[0]);
            if (p == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            else {
                sender.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.WHITE + " info:\n Profession: " + p.getProfession().getName());
                return true;
            }
        }
        else return false;
    }
}
