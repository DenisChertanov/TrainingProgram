package com.example.testbottomnavigationbar;

public class ExerciseSetResult {
    private int weight;
    private int repetitions;
    private int timer;

    public ExerciseSetResult(int weight, int repetitions, int timer) {
        this.weight = weight;
        this.repetitions = repetitions;
        this.timer = timer;
    }

    public int getWeight() {
        return weight;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getTimer() {
        return timer;
    }
}
