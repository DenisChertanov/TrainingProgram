package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class Exercise {
    @SerializedName("exerciseid")
    private int exerciseId;
    private String title;
    private String description;
    @SerializedName("videopath")
    private String videoPath;

    public Exercise() {

    }

    public Exercise(int exerciseId, String title, String description, String videoPath) {
        this.exerciseId = exerciseId;
        this.title = title;
        this.description = description;
        this.videoPath = videoPath;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO Exercise(ExerciseId, Title, Description, VideoPath)\n" +
                "VALUES (" +
                "'" +
                exerciseId +
                "', " +
                "'" +
                title +
                "', " +
                "'" +
                description +
                "', " +
                "'" +
                videoPath +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"exerciseid\" : " + exerciseId + ", " +
                "\"title\" : \"" + title + "\", " +
                "\"description\" : \"" + description + "\", " +
                "\"videopath\" : \"" + videoPath + "\" " +
                "}";
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoPath() {
        return videoPath;
    }
}
