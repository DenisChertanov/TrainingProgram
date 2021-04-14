package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class Training {
    @SerializedName("trainingid")
    private int trainingId;
    private final String title;
    @SerializedName("accountid")
    private int accountId;

    public Training(int trainingId, String title, int accountId) {
        this.trainingId = trainingId;
        this.title = title;
        this.accountId = accountId;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO Training(TrainingId, Title, AccountId)\n" +
                "VALUES (" +
                "'" +
                trainingId +
                "', " +
                "'" +
                title +
                "', " +
                "'" +
                accountId +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"trainingid\" : " + trainingId + ", " +
                "\"title\" : \"" + title + "\", " +
                "\"accountid\" : " + accountId + " " +
                "}";
    }

    public String getTitleJSON() {
        return "{ " +
                "\"title\" : \"" + title + "\"" +
                " }";
    }

    public int getTrainingId() {
        return trainingId;
    }

    public String getTitle() {
        return title;
    }
}
