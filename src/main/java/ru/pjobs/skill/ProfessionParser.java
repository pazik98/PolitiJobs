package ru.pjobs.skill;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProfessionParser {

    public ProfessionContainer parse(String path) {
        Gson gson = new GsonBuilder().create();

        try (FileReader reader = new FileReader(path)) {
            ProfessionContainer professionContainer = gson.fromJson(reader, ProfessionContainer.class);
            return professionContainer;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String toJson(ProfessionContainer professionContainer) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(professionContainer);
        return s;
    }
}
