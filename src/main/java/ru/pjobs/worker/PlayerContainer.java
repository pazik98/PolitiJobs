package ru.pjobs.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlayerContainer {

    Logger log = Logger.getLogger("Minecraft");

    private List<Player> players = new ArrayList<Player>();

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayerByName(String name) {
        for (Player player : this.players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayers(List<Player> players) {
        this.players.addAll(players);
    }
}
