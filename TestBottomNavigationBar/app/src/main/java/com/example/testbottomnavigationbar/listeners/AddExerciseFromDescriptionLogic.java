package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.remote_db.TrainingExercise;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingExerciseTask;

import java.io.IOException;

public class AddExerciseFromDescriptionLogic implements View.OnClickListener {
    private final TrainingDayOfWeekHelper trainingDayOfWeekHelper;
    private final String exerciseTitle;

    public AddExerciseFromDescriptionLogic(TrainingDayOfWeekHelper trainingDayOfWeekHelper, String exerciseTitle) {
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
        this.exerciseTitle = exerciseTitle;
    }

    @Override
    public void onClick(View v) {
        try {
            addExerciseInDB(v);
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Problem in adding exercise from description   " + e, e);
            }
        }

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void addExerciseInDB(View view) {
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "Trying adding exercise in DB");
        }

        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        int trainingId = trainingDayOfWeekHelper.getTrainingId();
        int dayOfWeek = trainingDayOfWeekHelper.getDayOfWeek();
        int orderNumber = getOrderNumber(db, trainingId, accountId, dayOfWeek);
        int exerciseId = SQLiteHelper.getExerciseIdFromTitle(db, exerciseTitle);

        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Timer, AccountId)\n" +
                "VALUES ('" + trainingId + "', '" + dayOfWeek + "', '" + orderNumber +
                "', '" + exerciseId + "', '" + 1 + "', '" + 0 + "', '" + 0 + "', '" + accountId + "');");

        addExerciseInRemoteDB(accountId, trainingId, dayOfWeek, orderNumber, exerciseId);
    }

    private void addExerciseInRemoteDB(int accountId, int trainingId, int dayOfWeek, int orderNumber, int exerciseId) {
        new InsertTrainingExerciseTask(new TrainingExercise(trainingId, dayOfWeek, orderNumber, exerciseId, 1,
                0, 0, accountId)).execute();
    }

    private int getOrderNumber(SQLiteDatabase db, int trainingId, int accountId, int dayOfWeek) {
        int orderNumber = 1;

        Cursor cursor = db.rawQuery("SELECT MAX(OrderNumber) FROM (SELECT OrderNumber FROM TrainingExercise " +
                "WHERE TrainingId = " + trainingId + " AND AccountId = " + accountId + " AND DayOfWeek = " + dayOfWeek + ") AS OrderTable;", null);
        if (cursor != null && cursor.moveToFirst()) {
            orderNumber = cursor.getInt(0) + 1;
        }

        return orderNumber;
    }
}
