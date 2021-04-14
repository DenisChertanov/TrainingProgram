package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class TrainingExercise {
    @SerializedName("trainingid")
    private final int trainingId;
    @SerializedName("dayofweek")
    private final int dayOfWeek;
    @SerializedName("ordernumber")
    private final int orderNumber;
    @SerializedName("exerciseid")
    private final int exerciseId;
    @SerializedName("setnumber")
    private final int setNumber;
    @SerializedName("repsnum")
    private final int repsNum;
    private final int timer;
    @SerializedName("accountid")
    private int accountId;

    public TrainingExercise(int trainingId, int dayOfWeek, int orderNumber, int exerciseId, int setNumber, int repsNum, int timer, int accountId) {
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.repsNum = repsNum;
        this.timer = timer;
        this.accountId = accountId;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Timer, AccountId)\n" +
                "VALUES (" +
                "'" +
                trainingId +
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
                setNumber +
                "', " +
                "'" +
                repsNum +
                "', " +
                "'" +
                timer +
                "', " +
                "'" +
                accountId +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"trainingid\" : " + trainingId + ", " +
                "\"dayofweek\" : " + dayOfWeek + ", " +
                "\"ordernumber\" : " + orderNumber + ", " +
                "\"exerciseid\" : " + exerciseId + ", " +
                "\"setnumber\" : " + setNumber + ", " +
                "\"repsnum\" : " + repsNum + ", " +
                "\"timer\" : " + timer + ", " +
                "\"accountid\" : " + accountId + " " +
                "}";
    }

    public int getTrainingId() {
        return trainingId;
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

    public int getSetNumber() {
        return setNumber;
    }

    public int getRepsNum() {
        return repsNum;
    }

    public int getTimer() {
        return timer;
    }

    public int getAccountId() {
        return accountId;
    }
}
