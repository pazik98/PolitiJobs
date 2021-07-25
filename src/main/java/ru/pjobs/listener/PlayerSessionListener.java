package ru.pjobs.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.worker.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerSessionListener implements Listener {

    PolitiJobsMain plugin;

    public PlayerSessionListener(PolitiJobsMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        String playerName = p.getName();

        // Loading information about player from db
        ru.pjobs.worker.Player DBPlayer;
        try {
            DBPlayer = plugin.getDb().getPlayer(playerName);
        } catch (Exception ex) {
            DBPlayer = null;
        }


        if (PlayerManager.playerContainer.getPlayerByName(playerName) == null && DBPlayer == null) {
            // Creating new player profile
            ru.pjobs.worker.Player newPlayer = new ru.pjobs.worker.Player(playerName);
            List<ru.pjobs.worker.Player> list = new ArrayList<>();
            list.add(newPlayer);
            PlayerManager.playerContainer.addPlayers(list);
        }
        else {
            // Using loaded player profile
            List<ru.pjobs.worker.Player> list = new ArrayList<>();
            list.add(DBPlayer);
            PlayerManager.playerContainer.addPlayers(list);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        String playerName = p.getName();

        // Remove player from list on quit
        PlayerManager.playerContainer.getPlayers().remove(PlayerManager.playerContainer.getPlayerByName(playerName));
    }
}
