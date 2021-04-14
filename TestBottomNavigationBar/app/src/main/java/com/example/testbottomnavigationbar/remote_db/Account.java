package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("accountid")
    private int accountId;
    @SerializedName("username")
    private String userName;
    @SerializedName("hashpassword")
    private String hashPassword;
    @SerializedName("birthdate")
    private String birthDate;
    @SerializedName("registrationdate")
    private String registrationDate;
    private String sex;
    @SerializedName("trainingid")
    private Integer trainingId;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("secondname")
    private String secondName;

    public Account() {

    }

    public Account(int accountId, String userName, String hashPassword, String birthDate, String registrationDate, String sex, Integer trainingId, String firstName, String secondName) {
        this.accountId = accountId;
        this.userName = userName;
        this.hashPassword = hashPassword;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.sex = sex;
        this.trainingId = trainingId;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO Account(AccountId, Username, HashPassword, BirthDate, RegistrationDate, Sex, TrainingId, FirstName, SecondName)\n" +
                "VALUES (" +
                "'" +
                accountId +
                "', " +
                "'" +
                userName +
                "', " +
                "'" +
                hashPassword +
                "', " +
                "'" +
                birthDate +
                "', " +
                "'" +
                registrationDate +
                "', " +
                "'" +
                sex +
                "', " +
                "'" +
                trainingId +
                "', " +
                "'" +
                firstName +
                "', " +
                "'" +
                secondName +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"accountid\" : " + accountId + ", " +
                "\"username\" : \"" + userName + "\", " +
                "\"hashpassword\" : \"" + hashPassword + "\", " +
                "\"birthdate\" : \"" + birthDate + "\", " +
                "\"registrationdate\" : \"" + registrationDate + "\", " +
                "\"sex\" : \"" + sex + "\", " +
                "\"trainingid\" : " + trainingId + ", " +
                "\"firstname\" : \"" + firstName + "\", " +
                "\"secondname\" : \"" + secondName + "\" " +
                "}";
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUserName() {
        return userName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getSex() {
        return sex;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }
}
