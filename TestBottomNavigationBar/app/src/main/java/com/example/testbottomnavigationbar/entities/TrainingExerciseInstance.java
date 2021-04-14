package com.example.testbottomnavigationbar.entities;

public class TrainingExerciseInstance {
    private final String exerciseTitle;
    private final int trainingId;
    private final int dayOfWeek;
    private final int orderNumber;

    public TrainingExerciseInstance(String exerciseTitle, int trainingId, int dayOfWeek, int orderNumber) {
        this.exerciseTitle = exerciseTitle;
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
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
}
