package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class TrainingExerciseSuccess {
    @SerializedName("accountid")
    private final int accountId;
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
    private final int weight;
    private final int timer;


    public TrainingExerciseSuccess(int accountId, int trainingId, int dayOfWeek, int orderNumber, int exerciseId, int setNumber, int repsNum, int weight, int timer) {
        this.accountId = accountId;
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.repsNum = repsNum;
        this.weight = weight;
        this.timer = timer;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO TrainingExerciseSuccess(AccountId, TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Weight, Timer)\n" +
                "VALUES (" +
                "'" +
                accountId +
                "', " +
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
                weight +
                "', " +
                "'" +
                timer +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"accountid\" : " + accountId + ", " +
                "\"trainingid\" : " + trainingId + ", " +
                "\"dayofweek\" : " + dayOfWeek + ", " +
                "\"ordernumber\" : " + orderNumber + ", " +
                "\"exerciseid\" : " + exerciseId + ", " +
                "\"setnumber\" : " + setNumber + ", " +
                "\"repsnum\" : " + repsNum + ", " +
                "\"weight\" : " + weight + ", " +
                "\"timer\" : " + timer + " " +
                "}";
    }

    public int getAccountId() {
        return accountId;
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

    public int getWeight() {
        return weight;
    }

    public int getTimer() {
        return timer;
    }
}
