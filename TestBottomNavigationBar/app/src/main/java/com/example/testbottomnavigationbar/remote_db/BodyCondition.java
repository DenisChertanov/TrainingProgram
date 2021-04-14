package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class BodyCondition {
    @SerializedName("accountid")
    private int accountId;
    private float weight;
    private float height;
    private int age;
    @SerializedName("bodyfatshare")
    private float bodyFatShare;

    public BodyCondition() {

    }

    public BodyCondition(int accountId, float weight, float height, int age, float bodyFatShare) {
        this.accountId = accountId;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.bodyFatShare = bodyFatShare;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO BodyCondition(AccountId, Weight, Height, Age, BodyFatShare)\n" +
                "VALUES (" +
                "'" +
                accountId +
                "', " +
                "'" +
                weight +
                "', " +
                "'" +
                height +
                "', " +
                "'" +
                age +
                "', " +
                "'" +
                bodyFatShare +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"accountid\" : " + accountId + ", " +
                "\"weight\" : " + weight + ", " +
                "\"height\" : " + height + ", " +
                "\"age\" : " + age + ", " +
                "\"bodyfatshare\" : " + bodyFatShare + " " +
                "}";
    }

    public int getAccountId() {
        return accountId;
    }

    public float getWeight() {
        return weight;
    }

    public float getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public float getBodyFatShare() {
        return bodyFatShare;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBodyFatShare(float bodyFatShare) {
        this.bodyFatShare = bodyFatShare;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
