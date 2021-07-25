package ru.pjobs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.pjobs.db.DatabaseSaver;
import ru.pjobs.db.SQLDatabase;
import ru.pjobs.listener.BlockDestroyListener;
import ru.pjobs.listener.PlayerSessionListener;
import ru.pjobs.listener.command.ProfessionCommand;
import ru.pjobs.skill.*;
import ru.pjobs.worker.PlayerContainer;
import ru.pjobs.worker.PlayerManager;

import java.io.File;
import java.util.logging.Logger;

public class PolitiJobsMain extends JavaPlugin {

    private Logger log = Logger.getLogger("Minecraft");
    private SQLDatabase db;
    private DatabaseSaver saver;

    @Override
    public void onEnable() {

        // Make plugin directory
        File pluginFolder = new File("plugins/PolitiJobs");
        pluginFolder.mkdir();

        // Create config file if isn't exists
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info("Creating new config file...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        // Create professions.json file if isn't exists
        File professionsConfig = new File(getDataFolder() + File.separator + "professions.json");
        if(!professionsConfig.exists()) {
            getLogger().info("Creating new professions file...");
            saveResource("professions.json", false);
        }

        // Reading professions.json
        ProfessionConfig.professions = new ProfessionParser().parse(getDataFolder() + File.separator + "professions.json");

        // Init player manager
        PlayerManager.playerContainer = new PlayerContainer();

        // Connecting database
        try {
            db = new SQLDatabase(this);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // Activating db saver
        try {
            saver = new DatabaseSaver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // List<String> blockList = new ArrayList<>();
        // blockList.add("GRASS_BLOCK");
        // AccessLevel al = new AccessLevel(1, blockList, blockList, blockList);
        // AccessLevel[] al_a = new AccessLevel[2];
        // al_a[0] = al;
        // Profession p = new Profession("miner", "Miner", al_a);
        // Profession[] p_a = new Profession[2];
        // p_a[0] = p;
        // p_a[1] = p;
        // ProfessionContainer pc = new ProfessionContainer();
        // pc.setProfessions(p_a);
        // ProfessionParser pp = new ProfessionParser();
        // String json = pp.toJson(pc);
        // log.info(json);


        Bukkit.getPluginManager().registerEvents(new PlayerSessionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockDestroyListener(this), this);

        getCommand("profession").setExecutor(new ProfessionCommand(this));
        getCommand("profession").setExecutor(new ProfessionCommand(this));

        log.info("[PJobs] Enabled!");
    }

    @Override
    public void onDisable() {
        saver.stop();
        saveDB();
        log.info("[PJobs] Disabled!");
    }

    public void saveDB() {
        db.saveData(PlayerManager.playerContainer.getPlayers());
    }

    public SQLDatabase getDb() {
        return db;
    }
}
