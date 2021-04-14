package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class TrainingExerciseNote {
    @SerializedName("trainingid")
    private final int trainingId;
    @SerializedName("accountid")
    private final int accountId;
    @SerializedName("dayofweek")
    private final int dayOfWeek;
    @SerializedName("ordernumber")
    private final int orderNumber;
    @SerializedName("exerciseid")
    private final int exerciseId;
    private final String note;

    public TrainingExerciseNote(int trainingId, int accountId, int dayOfWeek, int orderNumber, int exerciseId, String note) {
        this.trainingId = trainingId;
        this.accountId = accountId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
        this.exerciseId = exerciseId;
        this.note = note;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO TrainingExerciseNote(TrainingId, AccountId, DayOfWeek, OrderNumber, ExerciseId, Note)\n" +
                "VALUES (" +
                "'" +
                trainingId +
                "', " +
                "'" +
                accountId +
                "', " +
                "'" +
                dayOfWeek +
                "', " +
                "'" +
                orderNumber +
                "', " +
                "'" +
                exerciseId +
                "', " +
                "'" +
                note +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"trainingid\" : " + trainingId + ", " +
                "\"accountid\" : " + accountId + ", " +
                "\"dayofweek\" : " + dayOfWeek + ", " +
                "\"ordernumber\" : " + orderNumber + ", " +
                "\"exerciseid\" : " + exerciseId + ", " +
                "\"note\" : \"" + note + "\"" +
                "}";
    }

    public int getTrainingId() {
        return trainingId;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public String getNote() {
        return note;
    }
}
