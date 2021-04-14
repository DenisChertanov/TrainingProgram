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
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseNote;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseSuccess;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateCurrentTrainingTask;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateTrainingTask;

import java.util.ArrayList;
import java.util.List;

public class SetCurrentTrainingLogic implements View.OnClickListener {
    private final TrainingEditingHelper trainingEditingHelper;
    private List<TrainingExerciseSuccess> trainingExerciseSuccessList = new ArrayList<>();
    private List<TrainingExerciseNote> trainingExerciseNoteList = new ArrayList<>();

    public SetCurrentTrainingLogic(TrainingEditingHelper trainingEditingHelper) {
        this.trainingEditingHelper = trainingEditingHelper;
    }

    @Override
    public void onClick(View v) {
        try {
            updateCurrentTrainingInDB(v);
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Update current training value problems" + e, e);
            }
        }

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void updateCurrentTrainingInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int trainingId = trainingEditingHelper.getTrainingId();
        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        db.execSQL("UPDATE Account\n" +
                "SET TrainingId = " + trainingId + "\n" +
                "WHERE AccountId = " + accountId + ";");

        removeTrainingExerciseSuccessFromDB(db, accountId, trainingId);
        removeTrainingExerciseNoteFromDB(db, accountId, trainingId);
        addNewTrainingExerciseSuccess(db, accountId, trainingId);
        addNewTrainingExerciseNote(db, accountId, trainingId);

        updateCurrentTrainingInRemoteDB(accountId, trainingId);
    }

    private void updateCurrentTrainingInRemoteDB(int accountId, int trainingId) {
        new UpdateCurrentTrainingTask(accountId, trainingId, trainingExerciseSuccessList, trainingExerciseNoteList).execute();
    }

    private void removeTrainingExerciseSuccessFromDB(SQLiteDatabase db, int accountId, int trainingId) {
        db.execSQL("DELETE FROM TrainingExerciseSuccess\n" +
                "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + ";");
    }

    private void removeTrainingExerciseNoteFromDB(SQLiteDatabase db, int accountId, int trainingId) {
        db.execSQL("DELETE FROM TrainingExerciseNote\n" +
                "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + ";");
    }

    private void addNewTrainingExerciseSuccess(SQLiteDatabase db, int accountId, int trainingId) {
        Cursor cursor = db.rawQuery("SELECT * FROM TrainingExercise " +
                "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dayOfWeek = cursor.getInt(1);
                int orderNumber = cursor.getInt(2);
                int exerciseId = cursor.getInt(3);
                int setNumber = cursor.getInt(4);
                int repsNum = cursor.getInt(5);
                int timer = cursor.getInt(6);

                trainingExerciseSuccessList.add(new TrainingExerciseSuccess(accountId, trainingId, dayOfWeek, orderNumber, exerciseId, setNumber, repsNum, 0, timer));

                db.execSQL("INSERT INTO TrainingExerciseSuccess(AccountId, TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Weight, Timer)\n" +
                        "VALUES ('" + accountId + "', '" + trainingId + "', '" + dayOfWeek +
                        "', '" + orderNumber + "', '" + exerciseId + "', '" + setNumber + "', '" + repsNum + "', '" + 0 + "', '" + timer + "');");
            } while (cursor.moveToNext());
        }
    }

    private void addNewTrainingExerciseNote(SQLiteDatabase db, int accountId, int trainingId) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT DayOfWeek, OrderNumber, ExerciseId  FROM TrainingExercise " +
                "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dayOfWeek = cursor.getInt(0);
                int orderNumber = cursor.getInt(1);
                int exerciseId = cursor.getInt(2);

                trainingExerciseNoteList.add(new TrainingExerciseNote(trainingId, accountId, dayOfWeek, orderNumber, exerciseId, ""));

                db.execSQL("INSERT INTO TrainingExerciseNote(TrainingId, AccountId, DayOfWeek, OrderNumber, ExerciseId, Note)\n" +
                        "VALUES ('" + trainingId + "', '" + accountId + "', '" + dayOfWeek +
                        "', '" + orderNumber + "', '" + exerciseId + "', '');");
            } while (cursor.moveToNext());
        }
    }
}
