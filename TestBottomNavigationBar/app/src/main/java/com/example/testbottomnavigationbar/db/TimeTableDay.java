package com.example.testbottomnavigationbar.db;

import java.util.ArrayList;
import java.util.List;

public class TimeTableDay {
    private final int trainingId;
    private final int dayOfWeek;
    private final List<TimeTableExercise> exercises;

    public TimeTableDay(int trainingId, int dayOfWeek, List<TimeTableExercise> exercises) {
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.exercises = exercises;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStringDayOfWeek() {
        String[] ans = new String[] {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
        return ans[dayOfWeek];
    }

    public List<TimeTableExercise> getExercises() {
        return exercises;
    }

    public int getTrainingId() {
        return trainingId;
    }
}
