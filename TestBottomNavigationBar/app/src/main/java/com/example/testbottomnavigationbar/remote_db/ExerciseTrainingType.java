package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class ExerciseTrainingType {
    @SerializedName("typeid")
    private final int typeId;
    @SerializedName("exerciseid")
    private final int exerciseId;

    public ExerciseTrainingType(int typeId, int exerciseId) {
        this.typeId = typeId;
        this.exerciseId = exerciseId;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO ExerciseTrainingType(TypeId, ExerciseId)\n" +
                "VALUES (" +
                "'" +
                typeId +
                "', " +
                "'" +
                exerciseId +
                "');";
    }

    public int getTypeId() {
        return typeId;
    }

    public int getExerciseId() {
        return exerciseId;
    }
}
