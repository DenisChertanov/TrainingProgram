package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class DeleteMoveTrainingExeciseTask extends AsyncTask<Void, Void, Integer> {
    private final int type;
    private final int accountId;
    private final int maxOrderNumber;
    private final TrainingExerciseInstance trainingExerciseInstance;

    public DeleteMoveTrainingExeciseTask(int type, int accountId, int maxOrderNumber, TrainingExerciseInstance trainingExerciseInstance) {
        this.type = type;
        this.accountId = accountId;
        this.maxOrderNumber = maxOrderNumber;
        this.trainingExerciseInstance = trainingExerciseInstance;
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
                if (type == 0) {
                    worker.deleteTrainingExerciseSuccessWithOrderNumber(accountId, trainingExerciseInstance);
                    worker.deleteTrainingExerciseNoteWithOrderNumber(accountId, trainingExerciseInstance);

                    for (int i = trainingExerciseInstance.getOrderNumber() + 1; i <= maxOrderNumber; ++i) {
                        worker.updateTrainingExerciseSuccessWithNewOrderNumber(accountId, i - 1,
                                new TrainingExerciseInstance(null,
                                        trainingExerciseInstance.getTrainingId(),
                                        trainingExerciseInstance.getDayOfWeek(), i));

                        worker.updateTrainingExerciseNoteWithNewOrderNumber(accountId, i - 1,
                                new TrainingExerciseInstance(null,
                                        trainingExerciseInstance.getTrainingId(),
                                        trainingExerciseInstance.getDayOfWeek(), i));
                    }
                } else {
                    worker.deleteTrainingExerciseWithOrderNumber(accountId, trainingExerciseInstance);

                    for (int i = trainingExerciseInstance.getOrderNumber() + 1; i <= maxOrderNumber; ++i) {
                        worker.updateTrainingExerciseWithNewOrderNumber(accountId, i - 1,
                                new TrainingExerciseInstance(null,
                                        trainingExerciseInstance.getTrainingId(),
                                        trainingExerciseInstance.getDayOfWeek(), i));
                    }
                }
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with remove exercise:   " + e, e);
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error remove in remote db: " + e, e);
            }
            return 1;
        }
    }
}
