package com.example.testbottomnavigationbar.entities;

public class TrainingEditingHelper {
    private final int trainingId;
    private final String title;

    public TrainingEditingHelper(int trainingId, String title) {
        this.trainingId = trainingId;
        this.title = title;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public String getTitle() {
        return title;
    }
}
