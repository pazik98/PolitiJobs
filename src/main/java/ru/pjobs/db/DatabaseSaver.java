package ru.pjobs.db;

import ru.pjobs.PolitiJobsMain;

public class DatabaseSaver implements Runnable {
    PolitiJobsMain plugin;

    private boolean isRunning = true;

    public DatabaseSaver(PolitiJobsMain plugin) {
        this.plugin = plugin;
        new Thread(this, "DatabaseSaver").start();
    }

    @Override
    public void run() {
        while (isRunning) {
            plugin.saveDB();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
    }
}
