package ru.pjobs.skill;

import com.google.gson.annotations.SerializedName;

public class Profession {

    private String id;
    private String name;
    private AccessLevel[] accessLevels;

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

    public String toString() {
        String s = "Profession [" + this.id + "] named " + this.name + " and has " + this.accessLevels.length + " levels";
        return s;
    }
}
