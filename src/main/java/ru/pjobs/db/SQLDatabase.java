package ru.pjobs.db;

import org.bukkit.configuration.file.FileConfiguration;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.skill.ProfessionConfig;
import ru.pjobs.worker.Player;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLDatabase {

    private PolitiJobsMain plugin;

    private String user;
    private String password;
    private String url;

    public SQLDatabase(PolitiJobsMain plugin) throws Exception {
        this.plugin = plugin;

        FileConfiguration c = plugin.getConfig();

        Connection conn;

        if (c.getBoolean("mysql.enable")) {
            String host = c.getString("mysql.host");
            int port = c.getInt("mysql.port");
            String dbname = c.getString("mysql.dbname");

            user = c.getString("mysql.user");
            password = c.getString("mysql.password");
            url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;

            // Trying to connect MySQL
            try {
                Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
                conn = getConnection();
                plugin.getLogger().info("MySQL connected");
            } catch (Exception e) {
                // Connecting SQLite
                url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "database.db";
                Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
                user = null;
                conn = getConnection();
                plugin.getLogger().warning("Failed to connect to MySQL. Using SQLite...");
            }
        } else {
            // Connecting SQLite
            url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "database.db";
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
            conn = getConnection();
            plugin.getLogger().info("SQLite connected");
        }

        //conn = getConnection();
        Statement s = conn.createStatement();

        s.executeUpdate("CREATE TABLE IF NOT EXISTS player_job_account (`name` VARCHAR(32) not null, `job_id` VARCHAR(32), `level` INT, `experience` INT);");
        s.close();
        conn.close();
    }

    public Connection getConnection() throws SQLException {
        if (user != null) {
            return DriverManager.getConnection(url, user, password);
        }
        else {
            return DriverManager.getConnection(url);
        }
    }

    public void saveData(List<Player> players) {
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();

            for (Player player : players) {
                String name = player.getName();
                String professionId = player.getProfession().getId();
                int level = player.getLevel();
                int experience = player.getExperience();
                s.executeUpdate(String.format("INSERT INTO player_job_account (`name`, `job_id`, `level`, `experience`) VALUES ('%s', '%s', %d, %d);",
                        name, professionId, level, experience));
            }
            plugin.getLogger().info("DB Saved!");

            s.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer(String name) {
        Player player;
        try {
            Connection c = getConnection();
            Statement s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet result = s.executeQuery("SELECT * FROM player_job_account where name='" + name + "';");

            if (!result.next()) {
                return null;
            }

            result.first();
            player = new Player(result.getString("name"), ProfessionConfig.professions.getProfessionById(result.getString("job_id")),
                    result.getInt("level"), result.getInt("experience"));

            s.close();
            c.close();

            return player;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
