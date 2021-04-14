package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class UpdateNoteTask extends AsyncTask<Void, Void, Integer> {
    private final int accountId;
    private final int trainingId;
    private final int dayOfWeek;
    private final int orderNumber;
    private final String note;

    public UpdateNoteTask(int accountId, int trainingId, int dayOfWeek, int orderNumber, String note) {
        this.accountId = accountId;
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
        this.note = note;
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
                worker.updateNote(accountId, trainingId, dayOfWeek, orderNumber, note);
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with updating note:   " + e, e);
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
