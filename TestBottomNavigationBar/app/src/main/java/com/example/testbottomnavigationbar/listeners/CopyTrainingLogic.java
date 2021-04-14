package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.remote_db.Training;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.remote_db.TrainingExercise;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingTask;

import java.util.ArrayList;
import java.util.List;

public class CopyTrainingLogic implements View.OnClickListener {
    private final int friendAccountId;
    private final Training training;
    private String strTrainingTitle;

    public CopyTrainingLogic(int friendAccountId, Training training) {
        this.friendAccountId = friendAccountId;
        this.training = training;
    }

    @Override
    public void onClick(View v) {
        if (!accessName(v)) {
            return;
        }

        copyToDB(v);
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private void copyToDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
        int currentAccount = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        db.execSQL("INSERT INTO Training(Title, AccountId)\n" +
                "VALUES ('" + strTrainingTitle + "', '" + currentAccount + "');");

        List<TrainingExercise> trainingExerciseList = new ArrayList<>();
        int newTrainingId = SQLiteHelper.getTrainingIdFromTitle(db, strTrainingTitle, currentAccount);
        Training newTraining = new Training(newTrainingId, strTrainingTitle, currentAccount);

        Cursor cursor = db.rawQuery("SELECT * FROM TrainingExercise WHERE TrainingId = " + training.getTrainingId() + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                TrainingExercise trainingExercise = new TrainingExercise(
                        newTrainingId,
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6),
                        currentAccount
                );

                db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Timer, AccountId)\n" +
                        "VALUES ('" + newTrainingId + "', '" + trainingExercise.getDayOfWeek() + "', '" +
                        trainingExercise.getOrderNumber() + "', '" + trainingExercise.getExerciseId() + "', '" + trainingExercise.getSetNumber() + "', '" +
                        trainingExercise.getRepsNum() + "', '" + trainingExercise.getTimer() + "', '" + currentAccount + "');");

                trainingExerciseList.add(trainingExercise);
            } while (cursor.moveToNext());
        }

        copyToRemoteDB(trainingExerciseList, newTraining);
    }

    private void copyToRemoteDB(List<TrainingExercise> trainingExerciseList, Training training) {
        new InsertTrainingTask(training, 1, trainingExerciseList).execute();
    }

    private boolean accessName(View view) {
        EditText trainingTitle = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_training_title_et);
        strTrainingTitle = trainingTitle.getText().toString();

        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
        int currentAccount = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        return (!strTrainingTitle.equals("") && SQLiteHelper.getTrainingIdFromTitle(db, strTrainingTitle, currentAccount) == -1);
    }
}
