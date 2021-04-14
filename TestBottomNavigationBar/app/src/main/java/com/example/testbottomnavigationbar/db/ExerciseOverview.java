package com.example.testbottomnavigationbar.db;

import java.util.ArrayList;

public class ExerciseOverview {
    private String get_all_sorted_exercises_titles;
    private final ArrayList<String> tags;

    public ExerciseOverview(String title, ArrayList<String> tags) {
        get_all_sorted_exercises_titles = title;
        this.tags = tags;
    }

    public String getTitle() {
        return get_all_sorted_exercises_titles;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
