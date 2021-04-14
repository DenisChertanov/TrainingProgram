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
import com.example.testbottomnavigationbar.entities.TrainingEditingHelper;
import com.example.testbottomnavigationbar.remote_db.Training;
import com.example.testbottomnavigationbar.remote_db.tasks.UpdateTrainingTask;

public class EditTrainingTitleLogic implements View.OnClickListener {
    private String title = "";
    private final TrainingEditingHelper trainingEditingHelper;

    public EditTrainingTitleLogic(TrainingEditingHelper trainingEditingHelper) {
        this.trainingEditingHelper = trainingEditingHelper;
    }

    @Override
    public void onClick(View v) {
        if (!findTitle(v)) {
            return;
        }

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "onClick: title  " + title);
        }

        try {
            updateTrainingInDB(v);
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Update training value problems");
            }
        }

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
    }

    private boolean findTitle(View view) {
        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_training_editing_et);
        title = editText.getText().toString();

        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int trainingId = SQLiteHelper.getTrainingIdFromTitle(db, title, SQLiteHelper.getCurrentAccountId(currentAccountDB));

        return (!title.equals("") && trainingId == -1);
    }

    private void updateTrainingInDB(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = view.getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int trainingId = trainingEditingHelper.getTrainingId();

        db.execSQL("UPDATE Training\n" +
                "SET Title = '" + title + "'\n" +
                "WHERE TrainingId = " + trainingId + ";");

        updateTrainingInRemoteDB(currentAccountDB);
    }

    private void updateTrainingInRemoteDB(SQLiteDatabase currentAccountDB) {
        new UpdateTrainingTask(
                new Training(trainingEditingHelper.getTrainingId(), title, SQLiteHelper.getCurrentAccountId(currentAccountDB)).getTitleJSON(),
                trainingEditingHelper.getTrainingId()).execute();
    }
}
