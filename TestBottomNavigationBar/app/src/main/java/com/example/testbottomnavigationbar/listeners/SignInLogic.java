package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.MainFragment;

public class SignInLogic implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        if (checkInfo(v)) {
            ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            Fragment fragment = new MainFragment();
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.activity_main_fragment_cv, fragment, "mainFragment").commit();
            fTrans.addToBackStack(null);
        }
    }

    private boolean checkInfo(View view) {
        EditText username = ((FragmentActivity) view.getContext()).findViewById(R.id.login_layout_login_et);
        String strUsername = username.getText().toString();

        EditText password = ((FragmentActivity) view.getContext()).findViewById(R.id.login_layout_password_et);
        String strPassword = password.getText().toString();

        return checkAccountInDb(view.getContext(), strUsername, strPassword);
    }

    private boolean checkAccountInDb(Context context, String username, String password) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT AccountId FROM Account WHERE Username = '" + username + "' AND HashPassword = '" + password + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            int accountId = cursor.getInt(0);

            currentAccountDB.execSQL("INSERT INTO CurrentAccount(AccountId)\n" +
                    "VALUES ('" + accountId + "')");
            return true;
        }

        return false;
    }
}
