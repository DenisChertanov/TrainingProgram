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
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateNoteTask;

public class DoneNoteLogic implements View.OnClickListener {
    private final int accountId;
    private final TrainingExerciseInstance trainingExerciseInstance;
    private String note;

    public DoneNoteLogic(int accountId, TrainingExerciseInstance trainingExerciseInstance) {
        this.accountId = accountId;
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public void onClick(View v) {
        try {
            findNote(v);
            updateNoteInDB(v);
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Update note value problems");
            }
        }
    }

    private boolean findNote(View view) {
        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.training_exercise_success_note);
        note = editText.getText().toString();

        return true;
    }

    private void updateNoteInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        int trainingId = trainingExerciseInstance.getTrainingId();
        int dayOfWeek = trainingExerciseInstance.getDayOfWeek();
        int orderNumber = trainingExerciseInstance.getOrderNumber();

        db.execSQL("UPDATE TrainingExerciseNote\n" +
                "SET Note = '" + note + "'\n" +
                "WHERE TrainingId = " + trainingId +
                " AND AccountId = " + accountId +
                " AND DayOfWeek = " + dayOfWeek +
                " AND OrderNumber = " + orderNumber + ";");

        updateNoteInRemoteDB(db, trainingId, dayOfWeek, orderNumber);
    }

    private void updateNoteInRemoteDB(SQLiteDatabase db, int trainingId, int dayOfWeek, int orderNumber) {
        new UpdateNoteTask(accountId, trainingId, dayOfWeek, orderNumber, note).execute();
    }
}
