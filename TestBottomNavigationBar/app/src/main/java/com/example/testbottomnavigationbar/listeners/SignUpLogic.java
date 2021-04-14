package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.MainFragment;
import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;
import com.example.testbottomnavigationbar.remote_db.tasks.SignUpTask;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateAccountInfoTask;

public class SignUpLogic implements View.OnClickListener {
    private Account newAccount = new Account();
    private BodyCondition newBodyCondition = new BodyCondition();

    @Override
    public void onClick(View v) {
        if (!collectData(v)) {
            return;
        }

        addInfoInDB(v);

        Fragment fragment = new MainFragment();
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fTrans.replace(R.id.activity_main_fragment_cv, fragment, "mainFragment").commit();
        fTrans.addToBackStack(null);
    }

    private void addInfoInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        db.execSQL("INSERT INTO Account(Username, HashPassword, BirthDate, RegistrationDate, Sex, TrainingId, FirstName, SecondName)\n" +
                "VALUES ('" + newAccount.getUserName() + "', '" + newAccount.getHashPassword() + "', '2020-01-01', '2020-01-01', 'Мужской', null, '" + newAccount.getFirstName() + "', '" + newAccount.getSecondName() + "');");

        int accountId = -1;
        Cursor cursor = db.rawQuery("SELECT AccountId FROM Account WHERE Username = '" + newAccount.getUserName() + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            accountId = cursor.getInt(0);
            newAccount.setAccountId(accountId);
            newBodyCondition.setAccountId(accountId);
        }

        db.execSQL("INSERT INTO BodyCondition(AccountId, Weight, Height, Age, BodyFatShare)\n" +
                "VALUES ('" + accountId + "', '" + newBodyCondition.getWeight() + "', '" + newBodyCondition.getHeight() + "', '" + newBodyCondition.getAge() + "', '" + newBodyCondition.getBodyFatShare() + "');");

        currentAccountDB.execSQL("INSERT INTO CurrentAccount(AccountId)\n" +
                "VALUES ('" + accountId + "');");

        newAccount.setBirthDate("2020-01-01");
        newAccount.setRegistrationDate("2020-01-01");
        newAccount.setSex("Мужской");
        newAccount.setTrainingId(null);
        addInfoInRemoteDB();
    }

    private void addInfoInRemoteDB() {
        new SignUpTask(newAccount, newBodyCondition).execute();
    }

    private boolean collectData(View view) {
        return (collectFirstName(view) && collectSecondName(view) && collectUsername(view) && collectPassword(view) &&
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

        return (!strUsername.equals("") && SQLiteHelper.getAccountIdFromUsername(db, strUsername) == -1);
    }

    private boolean collectPassword(View view) {
        EditText password = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_sign_up_password_edit);
        String strPassword = password.getText().toString();
        newAccount.setHashPassword(strPassword);

        return !strPassword.equals("");
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
