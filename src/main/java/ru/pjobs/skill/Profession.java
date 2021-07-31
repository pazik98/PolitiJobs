package ru.pjobs.skill;

import java.util.ArrayList;
import java.util.List;

public class Profession {

    private String id;
    private String name;
    private AccessLevel[] accessLevels;

    private static List<Profession> config = new ArrayList<>();

    public Profession(String id, String name, AccessLevel[] accessLevels) {
        this.id = id;
        this.name = name;
        this.accessLevels = accessLevels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessLevel[] getAccessLevels() {
        return accessLevels;
    }

    public void setAccessLevels(AccessLevel[] accessLevels) {
        this.accessLevels = accessLevels;
    }

    public static List<Profession> getConfig() {
        return config;
    }

    public static void loadConfig(String path) {
        config = new ProfessionParser().parse(path);
    }

    public static Profession getById(String id) {
        for (Profession profession : config) {
            if (profession.getId().equals(id)) {
                return profession;
            }
        }
        return null;
    }

    public String toString() {
        String s = "Profession [" + this.id + "] named " + this.name + " and has " + this.accessLevels.length + " levels";
        return s;
    }
}
