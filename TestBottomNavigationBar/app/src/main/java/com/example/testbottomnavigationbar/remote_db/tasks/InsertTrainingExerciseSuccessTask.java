package com.example.testbottomnavigationbar.remote_db.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseSuccess;

import java.io.IOException;
import java.net.MalformedURLException;

public class InsertTrainingExerciseSuccessTask extends AsyncTask<Void, Void, Integer> {
    private final TrainingExerciseSuccess trainingExerciseSuccess;

    public InsertTrainingExerciseSuccessTask(TrainingExerciseSuccess trainingExerciseSuccess) {
        this.trainingExerciseSuccess = trainingExerciseSuccess;
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
                worker.insertTrainingExerciseSuccess(trainingExerciseSuccess);
            } catch (IOException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with inserting TrainingExerciseSuccess:   " + e, e);
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
