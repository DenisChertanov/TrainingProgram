package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.entities.InsertSetJSON;
import com.example.testbottomnavigationbar.remote_db.TrainingExercise;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseSuccess;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingExerciseSuccessTask;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingExerciseTask;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateTrainingExerciseSuccessTask;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateTrainingExerciseTask;

import java.io.IOException;

public class SetEditLogic implements View.OnClickListener {
    private final int type;
    private final ExerciseInTrainingEditing exerciseInTrainingEditing;

    public SetEditLogic(int type, ExerciseInTrainingEditing exerciseInTrainingEditing) {
        this.type = type;
        this.exerciseInTrainingEditing = exerciseInTrainingEditing;
    }

    @Override
    public void onClick(View v) {
        if (!checkInput(v)) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Bad input value to add set");
            }

            return;
        }

        try {
            addSetInDB(v);
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Add set value problems");
            }
        }
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void addSetInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        int trainingId = exerciseInTrainingEditing.getTrainingId();
        int dayOfWeek = exerciseInTrainingEditing.getDayOfWeek();
        int orderNumber = exerciseInTrainingEditing.getOrderNumber();
        int exerciseId = exerciseInTrainingEditing.getExerciseId();
        int setNumber = exerciseInTrainingEditing.getSetNumber();

        if (type == 0) {
            db.execSQL("UPDATE TrainingExerciseSuccess\n" +
                    "SET RepsNum = " + getRepsNum(view) + ", Weight = " + getWeight(view) + ", Timer = " + getTimer(view) + "\n" +
                    "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + " " +
                    "AND DayOfWeek = " + dayOfWeek + " AND OrderNumber = " + orderNumber + " AND SetNumber = " + setNumber + ";");
        } else {
            db.execSQL("UPDATE TrainingExercise\n" +
                    "SET RepsNum = " + getRepsNum(view) + ", Timer = " + getTimer(view) + "\n" +
                    "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + " " +
                    "AND DayOfWeek = " + dayOfWeek + " AND OrderNumber = " + orderNumber + " AND SetNumber = " + setNumber + ";");
        }

        addSetInRemoteDB(view);
    }

    private void addSetInRemoteDB(View view) {
        if (type == 0) {
            new UpdateTrainingExerciseSuccessTask(new InsertSetJSON(getWeight(view), getRepsNum(view), getTimer(view)), exerciseInTrainingEditing).execute();
        } else {
            new UpdateTrainingExerciseTask(new InsertSetJSON(-1, getRepsNum(view), getTimer(view)), exerciseInTrainingEditing).execute();
        }
    }

    private boolean checkInput(View view) {
        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
        EditText repsNum = (EditText) constraintLayout.getChildAt(4);
        EditText weight = (EditText) constraintLayout.getChildAt(1);
        EditText timer = (EditText) constraintLayout.getChildAt(7);

        if (!isNumber(repsNum.getText().toString()) || getRepsNum(view) < 0) {
            return false;
        }
        if (type == 0 && (!isNumber(weight.getText().toString()) || getWeight(view) < 0)) {
            return false;
        }
        if (!isNumber(timer.getText().toString()) || getTimer(view) < 0) {
            return false;
        }

        return true;
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

    private int getRepsNum(View view) {
        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
        EditText editText = (EditText) constraintLayout.getChildAt(4);

        return Integer.parseInt(editText.getText().toString());
    }

    private int getWeight(View view) {
        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
        EditText editText = (EditText) constraintLayout.getChildAt(1);

        return Integer.parseInt(editText.getText().toString());
    }

    private int getTimer(View view) {
        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
        EditText editText = (EditText) constraintLayout.getChildAt(7);

        return Integer.parseInt(editText.getText().toString());
    }
}
