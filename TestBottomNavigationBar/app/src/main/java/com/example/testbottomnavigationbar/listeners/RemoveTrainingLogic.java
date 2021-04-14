package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.TrainingEditingHelper;
import com.example.testbottomnavigationbar.remote_db.Training;
import com.example.testbottomnavigationbar.remote_db.tasks.DeleteTrainingTask;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateTrainingTask;

public class RemoveTrainingLogic implements View.OnClickListener{
    private final TrainingEditingHelper trainingEditingHelper;
    private boolean needSetNullCurrentTraining = false;

    public RemoveTrainingLogic(TrainingEditingHelper trainingEditingHelper) {
        this.trainingEditingHelper = trainingEditingHelper;
    }

    @Override
    public void onClick(View v) {
        try {
            removeTrainingInDB(v);
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Remove training value problems");
            }
        }

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void removeTrainingInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int trainingId = trainingEditingHelper.getTrainingId();

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "Delete trainingId:  " + trainingId);
            Log.d(MainActivity.TEG, "Delete local trainingId:  " + SQLiteHelper.getTrainingIdFromTitle(db, trainingEditingHelper.getTitle(), SQLiteHelper.getCurrentAccountId(currentAccountDB)));
        }

        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("DELETE FROM Training\n" +
                "WHERE TrainingId = " + trainingId + ";");

        if (MainActivity.LOG) {
            Cursor cursor = db.rawQuery("SELECT * FROM TrainingExercise WHERE TrainingId = " + trainingId + ";", null);
            Log.d(MainActivity.TEG, "After delete training:  " + (cursor != null && cursor.moveToFirst()));
        }

        setNullCurrentTraining(db, currentAccountDB);
        removeTrainingInRemoteDB(db, currentAccountDB);
    }

    private void removeTrainingInRemoteDB(SQLiteDatabase db, SQLiteDatabase currentAccountDB) {
        new DeleteTrainingTask(trainingEditingHelper.getTrainingId(), needSetNullCurrentTraining, SQLiteHelper.getCurrentAccountId(currentAccountDB)).execute();
    }

    private void setNullCurrentTraining(SQLiteDatabase db, SQLiteDatabase currentAccountDB) {
        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        int currentTrainingId = SQLiteHelper.getCurrentTrainingId(db, currentAccountDB);

        if (trainingEditingHelper.getTrainingId() == currentTrainingId) {
            needSetNullCurrentTraining = true;

            db.execSQL("UPDATE Account\n" +
                    "SET TrainingId = null\n" +
                    "WHERE AccountId = " + accountId + ";");
        }
    }
}
