package com.example.testbottomnavigationbar.entities;

public class ExerciseInTrainingEditing {
    private final int trainingId;
    private final int exerciseId;
    private final int orderNumber;
    private final int setNumber;
    private final int accountId;
    private final int dayOfWeek;

    public ExerciseInTrainingEditing(int trainingId, int exerciseId, int orderNumber, int setNumber, int accountId, int dayOfWeek) {
        this.trainingId = trainingId;
        this.exerciseId = exerciseId;
        this.orderNumber = orderNumber;
        this.setNumber = setNumber;
        this.accountId = accountId;
        this.dayOfWeek = dayOfWeek;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
}
