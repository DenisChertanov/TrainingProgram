package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.LoginFragment;
import com.example.testbottomnavigationbar.remote_db.tasks.DeleteAccountTask;

public class RemoveAccountLogic implements View.OnClickListener {
    private final int accountId;

    public RemoveAccountLogic(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public void onClick(View v) {
        removeAccountFromDB(v);

        SQLiteDatabase currentAccountDb = v.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
        currentAccountDb.execSQL("DELETE FROM CurrentAccount");

        Fragment fragment = new LoginFragment();
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fTrans.replace(R.id.activity_main_fragment_cv, fragment, "loginFragment").commit();
        fTrans.addToBackStack(null);
    }

    private void removeAccountFromDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        db.execSQL("PRAGMA foreign_keys=ON;");

        db.execSQL("DELETE FROM Account WHERE AccountId = " + accountId + ";");

        removeFriendFromRemoteDB();
    }

    private void removeFriendFromRemoteDB() {
        new DeleteAccountTask(accountId).execute();
    }
}
