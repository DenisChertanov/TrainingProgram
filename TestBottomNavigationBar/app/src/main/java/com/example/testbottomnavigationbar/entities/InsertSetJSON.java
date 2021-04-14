package com.example.testbottomnavigationbar.entities;

public class InsertSetJSON {
    private final int weight;
    private final int reps;
    private final int timer;

    public InsertSetJSON(int weight, int reps, int timer) {
        this.weight = weight;
        this.reps = reps;
        this.timer = timer;
    }

    public InsertSetJSON(int reps, int timer) {
        weight = -1;
        this.reps = reps;
        this.timer = timer;
    }

    public String getJSON() {
        if (weight != -1) {
            return "{ " +
                    "\"weight\" : " + weight + ", " +
                    "\"repsnum\" : " + reps + ", " +
                    "\"timer\" : " + timer +
                    " }";
        } else {
            return "{ " +
                    "\"repsnum\" : " + reps + ", " +
                    "\"timer\" : " + timer +
                    " }";
        }
    }
}
