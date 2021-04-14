package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class MoveTrainingExerciseTask extends AsyncTask<Void, Void, Integer> {
    private final int type;
    private final int accountId;
    private final int orderNumber;
    private final TrainingExerciseInstance trainingExerciseInstance;

    public MoveTrainingExerciseTask(int type, int accountId, int orderNumber, TrainingExerciseInstance trainingExerciseInstance) {
        this.type = type;
        this.accountId = accountId;
        this.orderNumber = orderNumber;
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
                    worker.updateTrainingExerciseSuccessWithNewOrderNumber(accountId, 100, trainingExerciseInstance);
                    worker.updateTrainingExerciseNoteWithNewOrderNumber(accountId, 100, trainingExerciseInstance);

                    if (orderNumber > trainingExerciseInstance.getOrderNumber()) {
                        for (int i = trainingExerciseInstance.getOrderNumber() + 1; i <= orderNumber; ++i) {
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
                        for (int i = trainingExerciseInstance.getOrderNumber() - 1; i >= orderNumber; --i) {
                            worker.updateTrainingExerciseSuccessWithNewOrderNumber(accountId, i + 1,
                                    new TrainingExerciseInstance(null,
                                            trainingExerciseInstance.getTrainingId(),
                                            trainingExerciseInstance.getDayOfWeek(), i));

                            worker.updateTrainingExerciseNoteWithNewOrderNumber(accountId, i + 1,
                                    new TrainingExerciseInstance(null,
                                            trainingExerciseInstance.getTrainingId(),
                                            trainingExerciseInstance.getDayOfWeek(), i));
                        }
                    }

                    worker.updateTrainingExerciseSuccessWithNewOrderNumber(accountId, orderNumber,
                            new TrainingExerciseInstance(trainingExerciseInstance.getExerciseTitle(),
                                    trainingExerciseInstance.getTrainingId(), trainingExerciseInstance.getDayOfWeek(), 100));
                    worker.updateTrainingExerciseNoteWithNewOrderNumber(accountId, orderNumber,
                            new TrainingExerciseInstance(trainingExerciseInstance.getExerciseTitle(),
                                    trainingExerciseInstance.getTrainingId(), trainingExerciseInstance.getDayOfWeek(), 100));
                } else {
                    worker.updateTrainingExerciseWithNewOrderNumber(accountId, 100, trainingExerciseInstance);

                    if (orderNumber > trainingExerciseInstance.getOrderNumber()) {
                        for (int i = trainingExerciseInstance.getOrderNumber() + 1; i <= orderNumber; ++i) {
                            worker.updateTrainingExerciseWithNewOrderNumber(accountId, i - 1,
                                    new TrainingExerciseInstance(null,
                                            trainingExerciseInstance.getTrainingId(),
                                            trainingExerciseInstance.getDayOfWeek(), i));
                        }
                    } else {
                        for (int i = trainingExerciseInstance.getOrderNumber() - 1; i >= orderNumber; --i) {
                            worker.updateTrainingExerciseWithNewOrderNumber(accountId, i + 1,
                                    new TrainingExerciseInstance(null,
                                            trainingExerciseInstance.getTrainingId(),
                                            trainingExerciseInstance.getDayOfWeek(), i));
                        }
                    }

                    worker.updateTrainingExerciseWithNewOrderNumber(accountId, orderNumber,
                            new TrainingExerciseInstance(trainingExerciseInstance.getExerciseTitle(),
                                    trainingExerciseInstance.getTrainingId(), trainingExerciseInstance.getDayOfWeek(), 100));
                }
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with move exercise:   " + e, e);
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error moving in remote db: " + e, e);
            }
            return 1;
        }
    }
}
