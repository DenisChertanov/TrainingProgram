package com.example.testbottomnavigationbar.entities;

public class TrainingDayOfWeekHelper {
    private final int trainingId;
    private final int dayOfWeek;

    public TrainingDayOfWeekHelper(int trainingId, int dayOfWeek) {
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
}
