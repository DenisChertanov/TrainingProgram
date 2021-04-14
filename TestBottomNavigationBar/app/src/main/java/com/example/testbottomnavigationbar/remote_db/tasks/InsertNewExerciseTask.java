package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.Exercise;
import com.example.testbottomnavigationbar.remote_db.ExerciseToTag;
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.TargetMuscle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class InsertNewExerciseTask extends AsyncTask<Void, Void, Integer> {
    Exercise exercise;
    List<ExerciseToTag> exerciseToTagList;
    List<TargetMuscle> targetMuscleList;

    public InsertNewExerciseTask(Exercise exercise, List<ExerciseToTag> exerciseToTagList, List<TargetMuscle> targetMuscleList) {
        this.exercise = exercise;
        this.exerciseToTagList = exerciseToTagList;
        this.targetMuscleList = targetMuscleList;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            HttpWork worker = null;
            try {
                worker = new HttpWork();
            } catch (MalformedURLException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with creating httpwork   " + e, e);
                }
            }
            try {
                worker.insertExercise(exercise);

                for (ExerciseToTag exerciseToTag : exerciseToTagList) {
                    worker.insertExerciseToTag(exerciseToTag);
                }

                for (TargetMuscle targetMuscle : targetMuscleList) {
                    worker.insertTargetMuscle(targetMuscle);
                }
            } catch (IOException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with inserting:   " + e, e);
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error inserting in remote db: " + e, e);
            }
            return 1;
        }
    }
}
