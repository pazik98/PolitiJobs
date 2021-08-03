package ru.pjobs.listener.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.skill.Profession;

import java.lang.module.Configuration;
import java.util.Locale;

public class ProfessionCommand implements CommandExecutor {

    private FileConfiguration config = PolitiJobsMain.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Check for arg count
        if (args.length < 1) {
            String m = config.getString("messages.commands.profession.unknown-command");
            m = m.replace("&", "\u00a7");
            sender.sendMessage(m);
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case ("set"):
            {
                // Check for command sender
                if (!(sender instanceof Player)) {
                    String m = config.getString("messages.commands.profession.set.console-sender");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Check for valid arguments
                if (args.length != 2) {
                    String m = config.getString("messages.commands.profession.set.wrong-arg");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                String professionId = args[1];

                // Check for existing of profession
                if (Profession.getById(professionId) == null) {
                    String m = config.getString("messages.commands.profession.set.prof-not-exist");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Check current profession
                ru.pjobs.worker.Player p = ru.pjobs.worker.Player.getFromOnlineListByName(sender.getName());
                if (p.getProfession() == Profession.getById(professionId)) {
                    String m = config.getString("messages.commands.profession.set.is-current");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Set profession
                p.setProfession(Profession.getById(professionId));

                // Output success message
                String m = config.getString("messages.commands.profession.set.success");
                m = m.replace("%profession%", Profession.getById(professionId).getName());
                m = m.replace("&", "\u00a7");
                sender.sendMessage(m);
                return true;
            }

            case ("list"):
            {
                // Check for valid arguments
                if (args.length != 1) {
                    String m = config.getString("messages.commands.profession.list.wrong-arg");
                    m = m.replace("&", "\u00a7");
                    sender.sendMessage(m);
                    return true;
                }

                // Output success message
                String m = config.getString("messages.commands.profession.list.success");

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
                String m = config.getString("messages.commands.profession.help");
                m = m.replace("&", "\u00a7");

                String commands = "\n- /p list\n- /p set";

                m = m.replace("%commands%", commands);
                sender.sendMessage(m);
                return true;
            }

            case("info"):
            {
                String m = config.getString("messages.commands.profession.info.profile");
                m = m.replace("&", "\u00a7");

                ru.pjobs.worker.Player player;
                if (args[1] != null) {
                    if (sender.hasPermission("command.profession.info.admin")) {
                        player = ru.pjobs.worker.Player.getFromOnlineListByName(args[1]);
                        if (player == null) {
                            sender.sendMessage(config.getString("messages.no-player"));
                            return true;
                        }
                    }
                    else {
                        sender.sendMessage(config.getString("messages.no-permission"));
                        return true;
                    }
                }
                else {
                    if (sender.hasPermission("command.profession.info")) {
                        player = ru.pjobs.worker.Player.getFromOnlineListByName(sender.getName());
                    }
                    else {
                        sender.sendMessage("messages.no-permission");
                        return true;
                    }
                }

                Profession p = player.getProfession();

                String name = player.getName();
                String profId;
                String level;
                String experience;

                if (p == null) {
                    profId = "None";
                    level = "0";
                    experience = "0";
                }
                else {
                    profId = p.getId();
                    level = Integer.toString(player.getLevel());
                    experience = Integer.toString(player.getExperience());
                }

                m = m.replace("%name%", name);
                m = m.replace("%profession%", profId);
                m = m.replace("%level%", level);
                m = m.replace("%exp%", experience);
                sender.sendMessage(m);

                return true;
            }

            default:
            {
                String m = config.getString("messages.commands.profession.unknown-command");
                m = m.replace("&", "\u00a7");
                sender.sendMessage(m);
                return true;
            }
        }
    }
}
