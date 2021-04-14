package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateAccountInfoTask;

public class SaveAccountEditingLogic implements View.OnClickListener {
    private final Account account;
    private final BodyCondition bodyCondition;
    private Account newAccount = new Account();
    private BodyCondition newBodyCondition = new BodyCondition();

    public SaveAccountEditingLogic(Account account, BodyCondition bodyCondition) {
        this.account = account;
        this.bodyCondition = bodyCondition;
    }

    @Override
    public void onClick(View v) {
        if (!collectData(v)) {
            return;
        }

        addInfoInDB(v);
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void addInfoInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        db.execSQL("UPDATE Account" +
                " SET FirstName = '" + newAccount.getFirstName() + "', SecondName = '" + newAccount.getSecondName() + "', UserName = '" + newAccount.getUserName() + "'" +
                " WHERE AccountId = " + account.getAccountId() + ";");

        db.execSQL("UPDATE BodyCondition" +
                " SET Age = " + newBodyCondition.getAge() + ", Weight = " + newBodyCondition.getWeight() + ", Height = " + newBodyCondition.getHeight() + ", BodyFatShare = " + newBodyCondition.getBodyFatShare() +
                " WHERE AccountId = " + account.getAccountId() + ";");

        addInfoInRemoteDB();
    }

    private void addInfoInRemoteDB() {
        new UpdateAccountInfoTask(account, bodyCondition, newAccount, newBodyCondition).execute();
    }

    private boolean collectData(View view) {
        return (collectFirstName(view) && collectSecondName(view) && collectUsername(view) &&
                collectAge(view) && collectWeight(view) && collectHeight(view) && collectBodyFatShare(view));
    }

    private boolean collectFirstName(View view) {
        EditText firstName = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_name_edit);
        String strFirstName = firstName.getText().toString();
        newAccount.setFirstName(strFirstName);

        return !strFirstName.equals("");
    }

    private boolean collectSecondName(View view) {
        EditText secondName = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_second_name_edit);
        String strSecondName = secondName.getText().toString();
        newAccount.setSecondName(strSecondName);

        return !strSecondName.equals("");
    }

    private boolean collectUsername(View view) {
        EditText username = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_second_username_edit);
        String strUsername = username.getText().toString();
        newAccount.setUserName(strUsername);
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        return (strUsername.equals(account.getUserName()) || (!strUsername.equals("") && SQLiteHelper.getAccountIdFromUsername(db, strUsername) == -1));
    }

    private boolean collectAge(View view) {
        EditText age = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_age_edit);
        String strAge = age.getText().toString();

        try {
            newBodyCondition.setAge(Integer.parseInt(strAge));
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "edittext:  " + e, e);
            }
            return false;
        }

        return (newBodyCondition.getAge() > 0);
    }

    private boolean collectWeight(View view) {
        EditText weight = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_weight_edit);
        String strWeight = weight.getText().toString();

        try {
            newBodyCondition.setWeight(Float.parseFloat(strWeight));
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "edittext:  " + e, e);
            }
            return false;
        }

        return (newBodyCondition.getWeight() > 0);
    }

    private boolean collectHeight(View view) {
        EditText height = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_height_edit);
        String strHeight = height.getText().toString();

        try {
            newBodyCondition.setHeight(Float.parseFloat(strHeight));
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "edittext:  " + e, e);
            }
            return false;
        }

        return (newBodyCondition.getHeight() > 0);
    }

    private boolean collectBodyFatShare(View view) {
        EditText bodyFatShare = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_percent_edit);
        String strBodyFatShare = bodyFatShare.getText().toString();

        try {
            newBodyCondition.setBodyFatShare(Float.parseFloat(strBodyFatShare));
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "edittext:  " + e, e);
            }
            return false;
        }

        return (newBodyCondition.getBodyFatShare() > 0);
    }
}
