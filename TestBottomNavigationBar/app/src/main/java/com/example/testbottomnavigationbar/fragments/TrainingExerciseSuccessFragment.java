package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.ExerciseSetResult;
import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.TrainingExerciseSuccessObj;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.listeners.DoneNoteLogic;
import com.example.testbottomnavigationbar.listeners.ExerciseSetLongClickLogic;
import com.example.testbottomnavigationbar.listeners.OpenExerciseFromQuestionLogic;
import com.example.testbottomnavigationbar.listeners.TrainingExerciseSuccessCreateSetLogic;

import java.util.List;

public class TrainingExerciseSuccessFragment extends Fragment {
    private final TrainingExerciseInstance trainingExerciseInstance;
    private TrainingExerciseSuccessObj trainingExerciseSuccessObj;

    public TrainingExerciseSuccessFragment(TrainingExerciseInstance trainingExerciseInstance) {
        super(R.layout.training_exercise_success_frame);

        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.training_exercise_success_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addBackButton(view);
        generateTrainingExerciseSuccess(view.getContext());
        addContent(view);
        tuneAddSet(view);
    }

    private void tuneAddSet(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        ConstraintLayout constraintLayout = ((FragmentActivity) view.getContext()).findViewById(R.id.training_exercise_success_addset_constrainlayout);
        constraintLayout.setOnClickListener(new TrainingExerciseSuccessCreateSetLogic(trainingExerciseInstance.getTrainingId(),
                trainingExerciseInstance.getDayOfWeek(), trainingExerciseInstance.getOrderNumber(),
                SQLiteHelper.getExerciseIdFromTitle(db, trainingExerciseInstance.getExerciseTitle()),
                trainingExerciseSuccessObj.getSetsInfo().size() + 1, 0));
    }

    private void addBackButton(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.my_toolbar_training_exercise_success);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void addContent(View view) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.training_exercise_success, (ViewGroup) view, false);
        LinearLayout linearLayout = (LinearLayout) constraintLayout.getChildAt(2);

        fillExerciseSets(linearLayout);
        setExerciseTitle((TextView) constraintLayout.getChildAt(0));

        ScrollView scrollView = ((FragmentActivity) view.getContext()).findViewById(R.id.training_exercise_success_scrollview);
        scrollView.addView(constraintLayout);

        tuneQuestionButton((ImageView) constraintLayout.getChildAt(1));

        tuneNotes(constraintLayout);
    }

    private void tuneNotes(ConstraintLayout constraintLayout) {
        SQLiteDatabase db = getView().getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = getView().getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        EditText editText = (EditText) constraintLayout.getChildAt(5);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
        fillNote(editText, db, accountId);

        TextView doneButton = (TextView) constraintLayout.getChildAt(6);
        doneButton.setOnClickListener(new DoneNoteLogic(accountId, trainingExerciseInstance));
    }

    private void fillNote(EditText editText, SQLiteDatabase db, int accountId) {
        Cursor cursor = db.rawQuery("SELECT Note FROM TrainingExerciseNote " +
                "WHERE TrainingId = " + trainingExerciseInstance.getTrainingId() +
                " AND Accountid = " + accountId +
                " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() +
                " AND OrderNumber = " + trainingExerciseInstance.getOrderNumber() + ";", null);

        String res = "";
        if (cursor != null && cursor.moveToFirst()) {
            String note = cursor.getString(0);
            if (note != null) {
                res = note;
            }
        }

        editText.setText(res);
    }

    private void tuneQuestionButton(ImageView imageView) {
        imageView.setOnClickListener(new OpenExerciseFromQuestionLogic(trainingExerciseInstance.getExerciseTitle()));
    }

    private void setExerciseTitle(TextView textView) {
        textView.setText(trainingExerciseInstance.getExerciseTitle());
    }

    private void fillExerciseSets(LinearLayout linearLayout) {
        SQLiteDatabase db = getView().getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = getView().getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        List<ExerciseSetResult> list = trainingExerciseSuccessObj.getSetsInfo();
        for (int i = 0; i < list.size(); ++i) {
            ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(linearLayout.getContext()).inflate(R.layout.set_info, linearLayout, false);
            constraintLayout.setOnLongClickListener(new ExerciseSetLongClickLogic(0,
                    new ExerciseInTrainingEditing(trainingExerciseInstance.getTrainingId(),
                            SQLiteHelper.getExerciseIdFromTitle(db, trainingExerciseInstance.getExerciseTitle()),
                            trainingExerciseInstance.getOrderNumber(),
                            i + 1,
                            SQLiteHelper.getCurrentAccountId(currentAccountDB),
                            trainingExerciseInstance.getDayOfWeek())));

            TextView setNum = (TextView) constraintLayout.getChildAt(0);
            setNum.setText("#" + (i + 1));

            TextView weight = (TextView) constraintLayout.getChildAt(5);
            if (list.get(i).getWeight() != 0) {
                weight.setText(list.get(i).getWeight() + " кг");
            } else {
                weight.setText("-  кг");
            }

            TextView repetitions = (TextView) constraintLayout.getChildAt(6);
            if (list.get(i).getRepetitions() != 0) {
                repetitions.setText(list.get(i).getRepetitions() + " раз");
            } else {
                repetitions.setText("-  раз");
            }

            TextView timer = (TextView) constraintLayout.getChildAt(7);
            if (list.get(i).getTimer() != 0) {
                timer.setText(list.get(i).getTimer() + " сек");
            } else {
                timer.setText("-  сек");
            }

            linearLayout.addView(constraintLayout);
        }
    }

    private void generateTrainingExerciseSuccess(Context context) {
        trainingExerciseSuccessObj = new TrainingExerciseSuccessObj(trainingExerciseInstance.getExerciseTitle());

        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        Cursor cursor = db.rawQuery("SELECT Weight, RepsNum, Timer, SetNumber FROM TrainingExerciseSuccess WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() + " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + " AND OrderNumber = " + trainingExerciseInstance.getOrderNumber() + " ORDER BY SetNumber;", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ExerciseSetResult exerciseSetResult = new ExerciseSetResult(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
                trainingExerciseSuccessObj.addExerciseSetResult(exerciseSetResult);
            } while (cursor.moveToNext());
        }
    }
}
