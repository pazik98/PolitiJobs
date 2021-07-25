package ru.pjobs.skill;

import java.util.ArrayList;
import java.util.List;

public final class ProfessionContainer {

    public List<Profession> professions = new ArrayList<Profession>();

    public List<Profession> getProfessions() {
        return professions;
    }

    public Profession getProfessionById(String id) {
        for (Profession profession : this.professions) {
            if (id.equals(profession.getId())) {
                return profession;
            }
        }
        return null;
    }

    public void setProfessions(List<Profession> p) {
        professions = p;
    }
}