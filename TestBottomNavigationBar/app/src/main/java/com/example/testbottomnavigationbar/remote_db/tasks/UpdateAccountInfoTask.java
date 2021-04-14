package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.net.MalformedURLException;

public class UpdateAccountInfoTask extends AsyncTask<Void, Void, Integer> {
    private final Account account;
    private final BodyCondition bodyCondition;
    private final Account newAccount;
    private final BodyCondition newBodyCondition;

    public UpdateAccountInfoTask(Account account, BodyCondition bodyCondition, Account newAccount, BodyCondition newBodyCondition) {
        this.account = account;
        this.bodyCondition = bodyCondition;
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
                worker.updateAccountInfo(account.getAccountId(), newAccount);
                worker.updateBodyConditionInfo(account.getAccountId(), newBodyCondition);
            } catch (Exception e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with update account info:   " + e, e);
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
