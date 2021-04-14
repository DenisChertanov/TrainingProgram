package com.example.testbottomnavigationbar;

import java.util.ArrayList;
import java.util.List;

public class TrainingExerciseSetsObj {
    private String exerciseTitle;
    private List<ExerciseSetResult> setsInfo;

    public TrainingExerciseSetsObj(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
        setsInfo = new ArrayList<>();
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public List<ExerciseSetResult> getSetsInfo() {
        return setsInfo;
    }

    public void addExerciseSetResult(ExerciseSetResult exerciseSetResult) {
        setsInfo.add(exerciseSetResult);
    }
}
