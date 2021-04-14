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
import com.example.testbottomnavigationbar.remote_db.Training;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingTask;


public class NewTrainingCreationLogic implements View.OnClickListener {
    private String title = "";

    @Override
    public void onClick(View v) {
        if (!findTitle(v)) {
            return;
        }

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "title   " + title);
        }

        addTrainingInDB(v);

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private boolean findTitle(View view) {
        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_training_title_et);
        title = editText.getText().toString();

        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int titleId = SQLiteHelper.getTrainingIdFromTitle(db, title, SQLiteHelper.getCurrentAccountId(currentAccountDB));

        return (!title.equals("") && titleId == -1);
    }

    private void addTrainingInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        db.execSQL("INSERT INTO Training(Title, AccountId)\n" +
                "VALUES ('" + title + "', '" + accountId + "');");

        addTrainingInRemoteDB(accountId, db);
    }

    private void addTrainingInRemoteDB(int accountId, SQLiteDatabase db) {
        new InsertTrainingTask(new Training(SQLiteHelper.getTrainingIdFromTitle(db, title, accountId), title, accountId), 0, null).execute();
    }
}
