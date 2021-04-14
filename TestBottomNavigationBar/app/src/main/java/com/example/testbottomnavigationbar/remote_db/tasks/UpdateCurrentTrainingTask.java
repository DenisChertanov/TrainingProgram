package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseNote;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseSuccess;

import java.net.MalformedURLException;
import java.util.List;

public class UpdateCurrentTrainingTask extends AsyncTask<Void, Void, Integer> {
    private final int accountId;
    private final int trainingId;
    private final List<TrainingExerciseSuccess> trainingExerciseSuccessList;
    private final List<TrainingExerciseNote> trainingExerciseNoteList;

    public UpdateCurrentTrainingTask(int accountId, int trainingId, List<TrainingExerciseSuccess> trainingExerciseSuccessList, List<TrainingExerciseNote> trainingExerciseNoteList) {
        this.accountId = accountId;
        this.trainingId = trainingId;
        this.trainingExerciseSuccessList = trainingExerciseSuccessList;
        this.trainingExerciseNoteList = trainingExerciseNoteList;
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
                worker.updateCurrentTraining(accountId, trainingId);

                worker.deleteTrainingExerciseSuccessFromTraining(accountId, trainingId);
                worker.deleteTrainingExerciseNote(accountId, trainingId);

                for (TrainingExerciseSuccess trainingExerciseSuccess : trainingExerciseSuccessList) {
                    worker.insertTrainingExerciseSuccess(trainingExerciseSuccess);
                }

                for (TrainingExerciseNote trainingExerciseNote : trainingExerciseNoteList) {
                    worker.insertTrainingExerciseNote(trainingExerciseNote);
                }
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with updating current training:   " + e, e);
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
