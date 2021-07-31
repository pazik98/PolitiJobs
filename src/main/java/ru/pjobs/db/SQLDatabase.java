package ru.pjobs.db;

import org.bukkit.configuration.file.FileConfiguration;
import ru.pjobs.PolitiJobsMain;
import ru.pjobs.skill.Profession;
import ru.pjobs.worker.Player;

import java.io.File;
import java.sql.*;
import java.util.List;

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

            // Get data from config
            String host = c.getString("mysql.host");
            int port = c.getInt("mysql.port");
            String dbname = c.getString("mysql.dbname");

            user = c.getString("mysql.user");
            password = c.getString("mysql.password");
            url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useSSL=false";

            // Trying to connect MySQL
            try {
                Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
                conn = getConnection();
                plugin.getLogger().info("MySQL connected");
            } catch (Exception e) {
                // Connecting SQLite
                plugin.getLogger().warning("Failed to connect to MySQL. Using SQLite...");
                url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "database.db";
                Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
                user = null;
                conn = getConnection();
            }
        } else {
            // Connecting SQLite
            url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "database.db";
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
            conn = getConnection();
            plugin.getLogger().info("SQLite connected");
        }

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

            for (Player player : players) {
                String name = player.getName();

                String professionId;
                if (!(player.getProfession() == null)) {
                    professionId = player.getProfession().getId();
                }
                else {
                    professionId = "";
                }

                int level = player.getLevel();
                int experience = player.getExperience();

                if (getPlayer(name) == null) {
                    Statement s = c.createStatement();
                    s.executeUpdate(String.format("INSERT INTO player_job_account (`name`, `job_id`, `level`, `experience`) VALUES ('%s', '%s', %d, %d);",
                            name, professionId, level, experience));
                    s.close();
                }
                else {
                    PreparedStatement ps = c.prepareStatement(String.format("UPDATE player_job_account SET job_id = '%s', level = '%d', experience = '%d' WHERE name = '%s';",
                            professionId, level, experience, name));
                    ps.executeUpdate();
                    ps.close();
                }
            }
            c.close();
            // plugin.getLogger().info("DB Saved!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer(String name) {
        Player player;
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();

            ResultSet result = s.executeQuery("SELECT * FROM player_job_account where name='" + name + "';");

            if (!result.next()) {
                return null;
            }
            result.first();

            String n = result.getString("name");
            Profession p = Profession.getById(result.getString("job_id"));
            int l = result.getInt("level");
            int e = result.getInt("experience");

            if (p == null) {
                player = new Player(n);
            }
            else {
                player = new Player(n, p, l, e);
            }

            s.close();
            c.close();

            return player;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
