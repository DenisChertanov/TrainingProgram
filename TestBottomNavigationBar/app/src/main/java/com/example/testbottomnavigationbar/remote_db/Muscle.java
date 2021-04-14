package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class Muscle {
    @SerializedName("muscleid")
    private final int muscleId;
    private final String title;

    public Muscle(int muscleId, String title) {
        this.muscleId = muscleId;
        this.title = title;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO Muscle(MuscleId, Title)\n" +
                "VALUES (" +
                "'" +
                muscleId +
                "', " +
                "'" +
                title +
                "');";
    }

    public int getMuscleId() {
        return muscleId;
    }

    public String getTitle() {
        return title;
    }
}
