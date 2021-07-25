package ru.pjobs.skill;

import java.util.List;

public class AccessLevel {

    private int experienceToNextLevel;
    private List<String> destroyList;
    private List<String> craftList;
    private List<String> enchantList;

    public AccessLevel(int experienceToNextLevel, List<String> destroyList, List<String> craftList, List<String> enchantList) {
        this.experienceToNextLevel = experienceToNextLevel;
        this.destroyList = destroyList;
        this.craftList = craftList;
        this.enchantList = enchantList;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public void setExperienceToNextLevel(int experienceToNextLevel) {
        this.experienceToNextLevel = experienceToNextLevel;
    }

    public List<String> getDestroyList() {
        return destroyList;
    }

    public void setDestroyList(List<String> destroyList) {
        this.destroyList = destroyList;
    }

    public List<String> getCraftList() {
        return craftList;
    }

    public void setCraftList(List<String> craftList) {
        this.craftList = craftList;
    }

    public List<String> getEnchantList() {
        return enchantList;
    }

    public void setEnchantList(List<String> enchantList) {
        this.enchantList = enchantList;
    }
}
