package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.Training;
import com.example.testbottomnavigationbar.remote_db.TrainingExercise;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class InsertTrainingTask extends AsyncTask<Void, Void, Integer> {
    private final Training training;
    private final int type;
    private final List<TrainingExercise> trainingExerciseList;

    public InsertTrainingTask(Training training, int type, List<TrainingExercise> trainingExerciseList) {
        this.training = training;
        this.type = type;
        this.trainingExerciseList = trainingExerciseList;
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
                worker.insertTraining(training);
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

    @Override
    protected void onPostExecute(Integer integer) {
        if (type == 1) {
            for (TrainingExercise trainingExercise : trainingExerciseList) {
                new InsertTrainingExerciseTask(trainingExercise).execute();
            }
        }
    }
}
