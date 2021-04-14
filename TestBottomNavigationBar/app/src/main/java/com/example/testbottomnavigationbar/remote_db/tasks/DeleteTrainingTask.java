package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class DeleteTrainingTask extends AsyncTask<Void, Void, Integer> {
    private final int trainingId;
    private final boolean needSetNullCurrentTraining;
    private final int accountId;

    public DeleteTrainingTask(int trainingId, boolean needSetNullCurrentTraining, int accountId) {
        this.trainingId = trainingId;
        this.needSetNullCurrentTraining = needSetNullCurrentTraining;
        this.accountId = accountId;
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
                if (needSetNullCurrentTraining) {
                    worker.setNullCurrentTraining(accountId);
                }

                worker.deleteTrainingFromId(trainingId);
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with removing Training:   " + e, e);
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
