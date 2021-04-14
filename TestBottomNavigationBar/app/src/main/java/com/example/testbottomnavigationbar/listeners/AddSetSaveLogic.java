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
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.TrainingExercise;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseSuccess;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingExerciseSuccessTask;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingExerciseTask;

import java.io.IOException;
import java.net.MalformedURLException;

public class AddSetSaveLogic implements View.OnClickListener {
    private final int trainingId;
    private final int dayOfWeek;
    private final int orderNumber;
    private final int exerciseId;
    private final int setNumber;
    private final int type;

    public AddSetSaveLogic(int trainingId, int dayOfWeek, int orderNumber, int exerciseId, int setNumber, int type) {
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.type = type;
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
        } catch (IOException e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Add set value problems");
            }
        }
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void addSetInDB(View view) throws IOException {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        if (type == 0) {
            db.execSQL("INSERT INTO TrainingExerciseSuccess(AccountId, TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Weight, Timer)\n" +
                    "VALUES ('" + accountId + "', '" + trainingId + "', '" + dayOfWeek + "', '" + orderNumber +
                    "', '" + exerciseId + "', '" + setNumber + "', '" + getRepsNum(view) + "', '" + getWeight(view) + "', '" + getTimer(view) + "');");
        } else {
            db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Timer, AccountId)\n" +
                    "VALUES ('" + trainingId + "', '" + dayOfWeek + "', '" + orderNumber +
                    "', '" + exerciseId + "', '" + setNumber + "', '" + getRepsNum(view) + "', '" + getTimer(view) + "', '" + accountId + "');");
        }

        addSetInRemoteDB(view, accountId);
    }

    private void addSetInRemoteDB(View view, int accountId) throws IOException {
        if (type == 0) {
            new InsertTrainingExerciseSuccessTask(new TrainingExerciseSuccess(accountId, trainingId, dayOfWeek, orderNumber,
                    exerciseId, setNumber, getRepsNum(view), getWeight(view), getTimer(view))).execute();
        } else {
            new InsertTrainingExerciseTask(new TrainingExercise(trainingId, dayOfWeek, orderNumber, exerciseId, setNumber,
                    getRepsNum(view), getTimer(view), accountId)).execute();
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
