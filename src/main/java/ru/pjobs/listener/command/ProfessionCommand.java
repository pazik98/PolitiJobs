package ru.pjobs.listener.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.skill.Profession;

import java.util.Locale;

public class ProfessionCommand implements CommandExecutor {

    private PolitiJobsMain plugin;

    public ProfessionCommand(PolitiJobsMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Check for arg count
        if (args.length < 1) {
            String m = plugin.getConfig().getString("messages.commands.profession.unknown-command");
            m = m.replace("&", "\u00a7");
            sender.sendMessage(m);
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case ("set"):
            {
                // Check for command sender
                if (!(sender instanceof Player)) {
                    String m = plugin.getConfig().getString("messages.commands.set.console-sender");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Check for valid arguments
                if (args.length != 2) {
                    String m = plugin.getConfig().getString("messages.commands.profession.set.wrong-arg");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                String professionId = args[1];

                // Check for existing of profession
                if (Profession.getById(professionId) == null) {
                    String m = plugin.getConfig().getString("messages.commands.profession.set.prof-not-exist");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Check current profession
                ru.pjobs.worker.Player p = ru.pjobs.worker.Player.getFromOnlineListByName(sender.getName());
                if (p.getProfession() == Profession.getById(professionId)) {
                    String m = plugin.getConfig().getString("messages.commands.profession.set.is-current");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Set profession
                p.setProfession(Profession.getById(professionId));

                // Output success message
                String m = plugin.getConfig().getString("messages.commands.profession.set.success");
                m = m.replace("%profession%", Profession.getById(professionId).getName());
                m = m.replace("&", "\u00a7");
                sender.sendMessage(m);
                return true;
            }

            case ("list"):
            {
                // Check for valid arguments
                if (args.length != 1) {
                    String m = plugin.getConfig().getString("messages.commands.profession.list.wrong-arg");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Output success message
                String m = plugin.getConfig().getString("messages.commands.profession.list.success");

                String profs = "";
                for (Profession pr : Profession.getConfig()) {
                    profs = profs + "\n" + pr.getId();
                }

                m = m.replace("&", "\u00a7");
                m = m.replace("%professions%", profs);
                sender.sendMessage(m);
                return true;
            }

            case("help"):
            {
                String m = plugin.getConfig().getString("messages.commands.profession.help");
                m = m.replace("&", "\u00a7");

                String commands = "\n/- p list\n/- p set";

                m = m.replace("%commands%", commands);
                sender.sendMessage(m);
                return true;
            }

            case("info"):
            {
                String m = plugin.getConfig().getString("messages.commands.profession.info");
                m = m.replace("&", "\u00a7");

                String profId = ru.pjobs.worker.Player.getFromOnlineListByName(sender.getName()).getProfession().getId();

                m = m.replace("%profession%", profId);
                sender.sendMessage(m);
                return true;
            }

            default:
            {
                String m = plugin.getConfig().getString("messages.commands.profession.unknown-command");
                m = m.replace("&", "\u00a7");
                sender.sendMessage(m);
                return true;
            }
        }
    }
}
