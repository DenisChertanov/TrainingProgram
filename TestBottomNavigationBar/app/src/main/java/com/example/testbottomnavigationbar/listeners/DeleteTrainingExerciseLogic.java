package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.remote_db.tasks.DeleteMoveTrainingExeciseTask;

public class DeleteTrainingExerciseLogic implements View.OnClickListener {
    private final int type;
    private final TrainingExerciseInstance trainingExerciseInstance;
    private int maxOrderNumber;

    public DeleteTrainingExerciseLogic(int type, TrainingExerciseInstance trainingExerciseInstance) {
        this.type = type;
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public void onClick(View v) {
        deleteTrainingExerciseInDB(v);

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void deleteTrainingExerciseInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        if (type == 0) {
            deleteInDB(db, accountId, "TrainingExerciseSuccess");
            deleteInDB(db, accountId, "TrainingExerciseNote");
        } else {
            deleteInDB(db, accountId, "TrainingExercise");
        }

        deleteTrainingExerciseInRemoteDB(accountId);
    }

    private void deleteInDB(SQLiteDatabase db, int accountId, String tableName) {
        // удаляем все сеты тренировки
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("DELETE FROM " + tableName +
                " WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + " AND OrderNumber = " + trainingExerciseInstance.getOrderNumber() + ";");

        // сдвинаем все сеты упражнений выше вниз
        getMaxOrderNumber(db, accountId, tableName);
        for (int i = trainingExerciseInstance.getOrderNumber() + 1; i <= maxOrderNumber; ++i) {
            db.execSQL("UPDATE " + tableName +
                    " SET OrderNumber = " + (i - 1) +
                    " WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                    " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() +
                    " AND OrderNumber = " + i + ";");
        }
    }

    private void deleteTrainingExerciseInRemoteDB(int accountId) {
        new DeleteMoveTrainingExeciseTask(type, accountId, maxOrderNumber, trainingExerciseInstance).execute();
    }

    private void getMaxOrderNumber(SQLiteDatabase db, int accountId, String tableName) {
        maxOrderNumber = -1;

        Cursor cursor = db.rawQuery("SELECT MAX(OrderNumber) " +
                "FROM " + tableName +
                " WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            maxOrderNumber = cursor.getInt(0);
        }
    }
}
