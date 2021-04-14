package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class ExerciseToTag {
    @SerializedName("exerciseid")
    private final int exerciseId;
    @SerializedName("tagid")
    private final int tagId;

    public ExerciseToTag(int exerciseId, int tagId) {
        this.exerciseId = exerciseId;
        this.tagId = tagId;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
                "VALUES (" +
                "'" +
                exerciseId +
                "', " +
                "'" +
                tagId +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"exerciseid\" : " + exerciseId + ", " +
                "\"tagid\" : " + tagId + " " +
                "}";
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getTagId() {
        return tagId;
    }
}
