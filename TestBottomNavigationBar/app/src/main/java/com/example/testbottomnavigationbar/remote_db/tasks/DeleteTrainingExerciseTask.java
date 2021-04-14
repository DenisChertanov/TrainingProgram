package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class DeleteTrainingExerciseTask extends AsyncTask<Void, Void, Integer> {
    private final ExerciseInTrainingEditing exerciseInTrainingEditing;

    public DeleteTrainingExerciseTask(ExerciseInTrainingEditing exerciseInTrainingEditing) {
        this.exerciseInTrainingEditing = exerciseInTrainingEditing;
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
                worker.deleteTrainingExercise(exerciseInTrainingEditing);
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with removing TrainingExerciseSuccess:   " + e, e);
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error removing in remote db: " + e, e);
            }
            return 1;
        }
    }
}
