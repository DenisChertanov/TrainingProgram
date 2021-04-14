package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.remote_db.tasks.MoveTrainingExerciseTask;

public class MoveTrainingExerciseLogic implements View.OnClickListener {
    private final int type;
    private final TrainingExerciseInstance trainingExerciseInstance;
    private int orderNumber = -1;

    public MoveTrainingExerciseLogic(int type, TrainingExerciseInstance trainingExerciseInstance) {
        this.type = type;
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public void onClick(View v) {
        if (!checkOrderNumber(v)) {
            return;
        }

        moveTrainingExerciseInDB(v);

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void moveTrainingExerciseInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        if (type == 0) {
            updateInDB(db, accountId, "TrainingExerciseSuccess");
            updateInDB(db, accountId, "TrainingExerciseNote");
        } else {
            updateInDB(db, accountId, "TrainingExercise");
        }

        moveTrainingExerciseInRemoteDB(accountId);
    }

    private void updateInDB(SQLiteDatabase db, int accountId, String tableName) {
        // Перемещаем все сеты далеко вперед
        db.execSQL("UPDATE " + tableName +
                " SET OrderNumber = 100 " +
                "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + " AND OrderNumber = " + trainingExerciseInstance.getOrderNumber() + ";");

        // Сдвигаем те, что между
        if (orderNumber > trainingExerciseInstance.getOrderNumber()) {
            for (int i = trainingExerciseInstance.getOrderNumber() + 1; i <= orderNumber; ++i) {
                db.execSQL("UPDATE " + tableName +
                        " SET OrderNumber = " + (i - 1) +
                        " WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                        " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() +
                        " AND OrderNumber = " + i + ";");
            }
        } else {
            for (int i = trainingExerciseInstance.getOrderNumber() - 1; i >= orderNumber; --i) {
                db.execSQL("UPDATE " + tableName +
                        " SET OrderNumber = " + (i + 1) +
                        " WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                        " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() +
                        " AND OrderNumber = " + i + ";");
            }
        }

        // Ставим нужное на новое место
        db.execSQL("UPDATE " + tableName +
                " SET OrderNumber = " + orderNumber +
                " WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + " AND OrderNumber = 100;");
    }

    private void moveTrainingExerciseInRemoteDB(int accountId) {
        new MoveTrainingExerciseTask(type, accountId, orderNumber, trainingExerciseInstance).execute();
    }

    private boolean checkOrderNumber(View view) {
        EditText orderNumberET = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_editing_training_exercise_et);

        if (isNumber(orderNumberET.getText().toString())) {
            int orderNumber = Integer.parseInt(orderNumberET.getText().toString());
            if (check(view, orderNumber)) {
                this.orderNumber = orderNumber;
                return true;
            }
        }

        return false;
    }

    private boolean check(View view, int orderNumber) {
        return (orderNumber > 0 && orderNumber <= getMaxOrderNumber(view) && orderNumber != trainingExerciseInstance.getOrderNumber());
    }

    private int getMaxOrderNumber(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        if (type == 0) {
            Cursor cursor = db.rawQuery("SELECT MAX(OrderNumber) " +
                    "FROM TrainingExerciseSuccess " +
                    "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                    " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + ";", null);

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } else {
            Cursor cursor = db.rawQuery("SELECT MAX(OrderNumber) " +
                    "FROM TrainingExercise " +
                    "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() +
                    " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + ";", null);

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        }

        return -1;
    }

    private boolean isNumber(String s) {
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "isNumber   " + s);
        }

        if (s.length() == 0) {
            return false;
        }

        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) - '0' < 0 || s.charAt(i) - '0' > 9) {
                return false;
            }
        }

        return true;
    }
}
