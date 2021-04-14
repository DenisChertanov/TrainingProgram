package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;
import com.example.testbottomnavigationbar.remote_db.ExerciseToTag;
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.TargetMuscle;

import java.io.IOException;
import java.net.MalformedURLException;

public class SignUpTask extends AsyncTask<Void, Void, Integer> {
    private final Account newAccount;
    private final BodyCondition newBodyCondition;

    public SignUpTask(Account newAccount, BodyCondition newBodyCondition) {
        this.newAccount = newAccount;
        this.newBodyCondition = newBodyCondition;
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
                worker.insertAccount(newAccount);
                worker.insertBodyCondition(newBodyCondition);
            } catch (IOException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with sign up:   " + e, e);
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error sign up in remote db: " + e, e);
            }
            return 1;
        }
    }
}
