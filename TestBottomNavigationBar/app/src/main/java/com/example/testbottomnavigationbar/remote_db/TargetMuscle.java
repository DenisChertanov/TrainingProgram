package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class TargetMuscle {
    @SerializedName("exerciseid")
    private final int exrciseId;
    @SerializedName("muscleid")
    private final int muscleId;

    public TargetMuscle(int exrciseId, int muscleId) {
        this.exrciseId = exrciseId;
        this.muscleId = muscleId;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO TargetMuscle(ExerciseId, MuscleId)\n" +
                "VALUES (" +
                "'" +
                exrciseId +
                "', " +
                "'" +
                muscleId +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"exerciseid\" : " + exrciseId + ", " +
                "\"muscleid\" : " + muscleId + " " +
                "}";
    }

    public int getExrciseId() {
        return exrciseId;
    }

    public int getMuscleId() {
        return muscleId;
    }
}
