package ru.pjobs.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.skill.AccessLevel;
import ru.pjobs.skill.Profession;
import ru.pjobs.skill.ProfessionConfig;
import ru.pjobs.skill.ProfessionContainer;
import ru.pjobs.worker.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BlockDestroyListener implements Listener {

    Logger log = Logger.getLogger("Minecraft");

    // Needed to check the need for verification in onBlockDestroy()
    private List<String> activeBlockList = new ArrayList<String>();

    public BlockDestroyListener(PolitiJobsMain plugin) {
        for (Profession profession : ProfessionConfig.professions.getProfessions()) {
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
            if (! PlayerManager.playerContainer.getPlayerByName(playerName).getAllowedDestroy().contains(blockName)) {
                e.getPlayer().sendMessage("Вы не можете ломать этот блок!");
                e.setDropItems(false);
            }
        }
    }
}