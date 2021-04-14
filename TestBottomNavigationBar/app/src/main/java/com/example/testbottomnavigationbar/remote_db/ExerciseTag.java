package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class ExerciseTag {
    @SerializedName("tagid")
    private final int tagId;
    private final String title;

    public ExerciseTag(int tagId, String title) {
        this.tagId = tagId;
        this.title = title;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO ExerciseTag(TagId, Title)\n" +
                "VALUES (" +
                "'" +
                tagId +
                "', " +
                "'" +
                title +
                "');";
    }

    public int getTagId() {
        return tagId;
    }

    public String getTitle() {
        return title;
    }
}
