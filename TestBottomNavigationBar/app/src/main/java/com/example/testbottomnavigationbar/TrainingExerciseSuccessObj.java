package com.example.testbottomnavigationbar;

import java.util.ArrayList;
import java.util.List;

public class TrainingExerciseSuccessObj {
    private String exerciseTitle;
    private List<ExerciseSetResult> setsInfo;
    private String note;

    public TrainingExerciseSuccessObj(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
        setsInfo = new ArrayList<>();
        note = "";
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public List<ExerciseSetResult> getSetsInfo() {
        return setsInfo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void addExerciseSetResult(ExerciseSetResult exerciseSetResult) {
        setsInfo.add(exerciseSetResult);
    }
}
