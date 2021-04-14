package com.example.testbottomnavigationbar.remote_db.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class UpdateTrainingTask extends AsyncTask<Void, Void, Integer> {
    private final String updateTrainingJSON;
    private final int trainingId;

    public UpdateTrainingTask(String updateTrainingJSON, int trainingId) {
        this.updateTrainingJSON = updateTrainingJSON;
        this.trainingId = trainingId;
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
                worker.updateTraining(updateTrainingJSON, trainingId);
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with updating Training:   " + e, e);
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
