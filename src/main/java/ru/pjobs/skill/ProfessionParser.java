package ru.pjobs.skill;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfessionParser {

    public List<Profession> parse(String path) {
        Gson gson = new GsonBuilder().create();

        try (FileReader reader = new FileReader(path)) {
            List<Profession> professions = gson.fromJson(reader, new TypeToken<List<Profession>>(){}.getType());
            return professions;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJson(List<Profession> professions) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(professions);
        return s;
    }
}
