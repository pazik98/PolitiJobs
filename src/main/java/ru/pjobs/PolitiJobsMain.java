package ru.pjobs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.pjobs.db.DatabaseSaver;
import ru.pjobs.db.SQLDatabase;
import ru.pjobs.listener.BlockDestroyListener;
import ru.pjobs.listener.PlayerSessionListener;
import ru.pjobs.listener.command.ProfessionCommand;
import ru.pjobs.skill.*;
import ru.pjobs.worker.Player;

import java.io.File;
import java.util.logging.Logger;

public class PolitiJobsMain extends JavaPlugin {

    private static PolitiJobsMain instance;

    private SQLDatabase db;
    private DatabaseSaver saver;

    public PolitiJobsMain() {
        instance = this;
    }

    public static PolitiJobsMain getInstance() {
        return instance;
    }

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
        Profession.loadConfig(getDataFolder() + File.separator + "professions.json");

        // Connecting database
        try {
            db = new SQLDatabase(this);
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("Cannot enable database!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // Activating db saver
        try {
            saver = new DatabaseSaver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new PlayerSessionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockDestroyListener(this), this);

        getCommand("profession").setExecutor(new ProfessionCommand(this));

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        saver.stop();
        saveDB();
        getLogger().info("Plugin disabled!");
    }

    public void saveDB() {
        db.saveData(Player.getOnlineList());
    }

    public SQLDatabase getDb() {
        return db;
    }
}
