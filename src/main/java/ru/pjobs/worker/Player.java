package ru.pjobs.worker;

import ru.pjobs.PolitiJobsMain;
import ru.pjobs.db.SQLDatabase;
import ru.pjobs.skill.Profession;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private Profession profession;
    private int level;
    private int experience;

    private List<String> allowedDestroy = new ArrayList<>();
    private List<String> allowedCraft = new ArrayList<>();
    private List<String> allowedEnchant = new ArrayList<>();

    private static List<Player> onlineList = new ArrayList<>();

    public Player(String name, Profession profession, int level, int experience) {
        this.name = name;
        this.profession = profession;
        this.level = level;
        this.experience = experience;

        this.updateAllowedLists();
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

    public static List<Player> getOnlineList() {
        return onlineList;
    }

    public static Player getFromOnlineListByName(String name) {
        for (Player player : onlineList) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
        this.level = 1;
        this.experience = 0;

        this.allowedDestroy = new ArrayList<>();
        this.allowedCraft = new ArrayList<>();
        this.allowedEnchant = new ArrayList<>();

        this.updateAllowedLists();

        PolitiJobsMain.getInstance().getDb();
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

    public void addToOnlineList() {
        onlineList.add(this);
    }

    public static void addListToOnlineList(List<Player> list) {
        onlineList.addAll(list);
    }

    public void removeFromOnlineList() {
        onlineList.remove(this);
    }

    public static void removeFromOnlineListByName(String name) {
        Player player = getFromOnlineListByName(name);
        player.removeFromOnlineList();
    }

    public void levelUp() {
        if (this.profession != null) {
            this.level += 1;
            this.updateAllowedLists();
        }
    }

    public void updateAllowedLists() {
        this.allowedDestroy.addAll(this.profession.getAccessLevels()[this.level-1].getDestroyList());
        this.allowedCraft.addAll(this.profession.getAccessLevels()[this.level-1].getCraftList());
        this.allowedEnchant.addAll(this.profession.getAccessLevels()[this.level-1].getEnchantList());
    }
}
