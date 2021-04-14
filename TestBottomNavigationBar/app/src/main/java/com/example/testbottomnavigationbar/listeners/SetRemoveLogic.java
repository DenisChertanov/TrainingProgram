package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.remote_db.tasks.DeleteTrainingExerciseSuccessTask;
import com.example.testbottomnavigationbar.remote_db.tasks.DeleteTrainingExerciseTask;

public class SetRemoveLogic implements View.OnClickListener {
    private final int type;
    private final ExerciseInTrainingEditing exerciseInTrainingEditing;

    public SetRemoveLogic(int type, ExerciseInTrainingEditing exerciseInTrainingEditing) {
        this.type = type;
        this.exerciseInTrainingEditing = exerciseInTrainingEditing;
    }

    @Override
    public void onClick(View v) {
        try {
            removeSetInDB(v);
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Remove set value problems");
            }
        }

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void removeSetInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        int trainingId = exerciseInTrainingEditing.getTrainingId();
        int dayOfWeek = exerciseInTrainingEditing.getDayOfWeek();
        int orderNumber = exerciseInTrainingEditing.getOrderNumber();
        int exerciseId = exerciseInTrainingEditing.getExerciseId();
        int setNumber = exerciseInTrainingEditing.getSetNumber();

        if (type == 0) {
            db.execSQL("DELETE FROM TrainingExerciseSuccess\n" +
                    "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + " " +
                    "AND DayOfWeek = " + dayOfWeek + " AND OrderNumber = " + orderNumber + " AND SetNumber = " + setNumber + ";");
        } else {
            db.execSQL("DELETE FROM TrainingExercise\n" +
                    "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + " " +
                    "AND DayOfWeek = " + dayOfWeek + " AND OrderNumber = " + orderNumber + " AND SetNumber = " + setNumber + ";");
        }

        removeSetInRemoteDB();
    }

    private void removeSetInRemoteDB() {
        if (type == 0) {
            new DeleteTrainingExerciseSuccessTask(exerciseInTrainingEditing).execute();
        } else {
            new DeleteTrainingExerciseTask(exerciseInTrainingEditing).execute();
        }
    }
}
