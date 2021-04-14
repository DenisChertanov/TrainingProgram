package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.TrainingExercise;

import java.io.IOException;
import java.net.MalformedURLException;

public class InsertTrainingExerciseTask extends AsyncTask<Void, Void, Integer> {
    private final TrainingExercise trainingExercise;

    public InsertTrainingExerciseTask(TrainingExercise trainingExercise) {
        this.trainingExercise = trainingExercise;
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
                worker.insertTrainingExercise(trainingExercise);
            } catch (IOException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with inserting TrainingExercise:   " + e, e);
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
