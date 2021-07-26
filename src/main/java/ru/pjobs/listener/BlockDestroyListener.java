package ru.pjobs.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.skill.AccessLevel;
import ru.pjobs.skill.Profession;
import ru.pjobs.worker.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BlockDestroyListener implements Listener {

    Logger log = Logger.getLogger("Minecraft");

    // Needed to check the need for verification in onBlockDestroy()
    private List<String> activeBlockList = new ArrayList<>();

    public BlockDestroyListener(PolitiJobsMain plugin) {
        for (Profession profession : Profession.getConfig()) {
            for (AccessLevel accessLevel : profession.getAccessLevels()) {
                if (accessLevel != null)
                    activeBlockList.addAll(accessLevel.getDestroyList());
            }
        }
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        String playerName = e.getPlayer().getName();
        String blockName = e.getBlock().getBlockData().getMaterial().name();

        if (activeBlockList.contains(blockName)) {
            if (!Player.getFromOnlineListByName(playerName).getAllowedDestroy().contains(blockName)) {
                e.getPlayer().sendMessage("Вы не можете ломать этот блок!");
                e.setDropItems(false);
            }
        }
    }
}