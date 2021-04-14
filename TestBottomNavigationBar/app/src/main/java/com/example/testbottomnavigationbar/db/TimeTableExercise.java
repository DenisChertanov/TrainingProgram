package com.example.testbottomnavigationbar.db;

public class TimeTableExercise {
    private final String exerciseTitle;
    private final boolean isLastExercise;
    private final int orderNumber;

    public TimeTableExercise(String exerciseTitle, boolean isLastExercise, int orderNumber) {
        this.exerciseTitle = exerciseTitle;
        this.isLastExercise = isLastExercise;
        this.orderNumber = orderNumber;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public boolean isLastExercise() {
        return isLastExercise;
    }

    public int getOrderNumber() {
        return orderNumber;
    }
}
