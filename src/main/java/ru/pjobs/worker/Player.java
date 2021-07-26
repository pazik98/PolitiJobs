package ru.pjobs.worker;

import ru.pjobs.skill.Profession;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private Profession profession;
    private int level;
    private int experience;

    private List<String> allowedDestroy = new ArrayList<String>();
    private List<String> allowedCraft = new ArrayList<String>();
    private List<String> allowedEnchant = new ArrayList<String>();

    public Player(String name, Profession profession, int level, int experience) {
        this.name = name;
        this.profession = profession;
        this.level = level;
        this.experience = experience;

        this.allowedDestroy.addAll(profession.getAccessLevels()[level-1].getDestroyList());
        this.allowedCraft.addAll(profession.getAccessLevels()[level-1].getCraftList());
        this.allowedEnchant.addAll(profession.getAccessLevels()[level-1].getEnchantList());
    }

    public Player(String name) {
        this.name = name;
        this.profession = null;
        this.level = 0;
        this.experience = 0;
    }

    public String getName() {
        return name;
    }

    public Profession getProfession() {
        return profession;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getAllowedDestroy() {
        return allowedDestroy;
    }

    public List<String> getAllowedCraft() {
        return allowedCraft;
    }

    public List<String> getAllowedEnchant() {
        return allowedEnchant;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
        this.level = 1;
        this.experience = 0;

        this.allowedDestroy = new ArrayList<>();
        this.allowedDestroy.addAll(this.profession.getAccessLevels()[this.level-1].getDestroyList());

        this.allowedCraft = new ArrayList<>();
        this.allowedCraft.addAll(this.profession.getAccessLevels()[this.level-1].getCraftList());

        this.allowedEnchant = new ArrayList<>();
        this.allowedEnchant.addAll(this.profession.getAccessLevels()[this.level-1].getEnchantList());
    }

    public void setAllowedDestroy(List<String> allowedDestroy) {
        this.allowedDestroy = allowedDestroy;
    }

    public void addAllowedDestroy(List<String> allowedDestroy) {
        this.allowedDestroy.addAll(allowedDestroy);
    }

    public void setAllowedCraft(List<String> allowedCraft) {
        this.allowedCraft = allowedCraft;
    }

    public void addAllowedCraft(List<String> allowedCraft) {
        this.allowedCraft.addAll(allowedCraft);
    }

    public void setAllowedEnchant(List<String> allowedEnchant) {
        this.allowedEnchant = allowedEnchant;
    }

    public void addAllowedEnchant(List<String> allowedEnchant) {
        this.allowedEnchant.addAll(allowedEnchant);
    }

    public void addExperince(int experience) {
        if (this.profession == null) {
            return;
        }

        this.experience += experience;

        // Check for level up
        int requiredExp = this.profession.getAccessLevels()[this.level-1].getExperienceToNextLevel();
        if (this.experience >= requiredExp) {
            int remain = this.experience - requiredExp;
            this.levelUp();
            this.experience = remain;
        }
    }

    public void levelUp() {
        if (this.profession != null) {
            this.level += 1;
            this.allowedDestroy.addAll(this.profession.getAccessLevels()[this.level-1].getDestroyList());
            this.allowedCraft.addAll(this.profession.getAccessLevels()[this.level-1].getCraftList());
            this.allowedEnchant.addAll(this.profession.getAccessLevels()[this.level-1].getEnchantList());
        }
    }
}
