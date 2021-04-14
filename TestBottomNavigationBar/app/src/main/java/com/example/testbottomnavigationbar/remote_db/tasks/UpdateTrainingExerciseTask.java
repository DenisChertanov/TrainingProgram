package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.entities.InsertSetJSON;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class UpdateTrainingExerciseTask extends AsyncTask<Void, Void, Integer> {
    private final InsertSetJSON insertSetJSON;
    private final ExerciseInTrainingEditing exerciseInTrainingEditing;

    public UpdateTrainingExerciseTask(InsertSetJSON insertSetJSON, ExerciseInTrainingEditing exerciseInTrainingEditing) {
        this.insertSetJSON = insertSetJSON;
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
                worker.updateTrainingExercise(insertSetJSON, exerciseInTrainingEditing);
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with updating TrainingExercise:   " + e, e);
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error updating in remote db: " + e, e);
            }
            return 1;
        }
    }
}
