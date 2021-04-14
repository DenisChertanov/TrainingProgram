package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
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
import com.example.testbottomnavigationbar.TrainingExerciseSetsObj;
import com.example.testbottomnavigationbar.TrainingExerciseSuccessObj;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.listeners.ExerciseSetLongClickLogic;
import com.example.testbottomnavigationbar.listeners.OpenExerciseFromQuestionLogic;
import com.example.testbottomnavigationbar.listeners.TrainingExerciseSuccessCreateSetLogic;

import java.util.List;

public class TrainingExerciseSetsFragment extends Fragment {
    private final int type;
    private final int accountId;
    private final TrainingExerciseInstance trainingExerciseInstance;
    private TrainingExerciseSetsObj trainingExerciseSetsObj;

    public TrainingExerciseSetsFragment(int type, int accountId, TrainingExerciseInstance trainingExerciseInstance) {
        super(R.layout.training_exercise_success_frame);
        this.type = type;
        this.accountId = accountId;
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.training_exercise_success_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addBackButton(view);
        generateTrainingExerciseSets(view.getContext());
        addContent(view);
        tuneAddSet(view);
    }

    private void tuneAddSet(View view) {
        ConstraintLayout constraintLayout = ((FragmentActivity) view.getContext()).findViewById(R.id.training_exercise_success_addset_constrainlayout);

        if (type != 1) {
            constraintLayout.setVisibility(View.GONE);
            return;
        }

        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        constraintLayout.setOnClickListener(new TrainingExerciseSuccessCreateSetLogic(trainingExerciseInstance.getTrainingId(),
                trainingExerciseInstance.getDayOfWeek(), trainingExerciseInstance.getOrderNumber(),
                SQLiteHelper.getExerciseIdFromTitle(db, trainingExerciseInstance.getExerciseTitle()),
                trainingExerciseSetsObj.getSetsInfo().size() + 1, 1));
    }

    private void addContent(View view) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.training_exercise_success, (ViewGroup) view, false);
        LinearLayout linearLayout = (LinearLayout) constraintLayout.getChildAt(2);

        fillExerciseSets(linearLayout);
        setExerciseTitle((TextView) constraintLayout.getChildAt(0));

        ScrollView scrollView = ((FragmentActivity) view.getContext()).findViewById(R.id.training_exercise_success_scrollview);
        scrollView.addView(constraintLayout);

        // Hide note
        TextView textView = (TextView) constraintLayout.getChildAt(4);
        textView.setVisibility(View.GONE);
        EditText editText = (EditText) constraintLayout.getChildAt(5);
        editText.setVisibility(View.GONE);
        TextView textView2 = (TextView) constraintLayout.getChildAt(6);
        textView2.setVisibility(View.GONE);

        tuneQuestionButton((ImageView) constraintLayout.getChildAt(1));
    }

    private void tuneQuestionButton(ImageView imageView) {
        imageView.setOnClickListener(new OpenExerciseFromQuestionLogic(trainingExerciseInstance.getExerciseTitle()));
    }

    private void setExerciseTitle(TextView textView) {
        textView.setText(trainingExerciseInstance.getExerciseTitle());
    }

    private void fillExerciseSets(LinearLayout linearLayout) {
        SQLiteDatabase db = getView().getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
//        SQLiteDatabase currentAccountDB = getView().getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        List<ExerciseSetResult> list = trainingExerciseSetsObj.getSetsInfo();
        for (int i = 0; i < list.size(); ++i) {
            ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(linearLayout.getContext()).inflate(R.layout.set_info, linearLayout, false);

            if (type == 1) {
                constraintLayout.setOnLongClickListener(new ExerciseSetLongClickLogic(1,
                        new ExerciseInTrainingEditing(trainingExerciseInstance.getTrainingId(),
                                SQLiteHelper.getExerciseIdFromTitle(db, trainingExerciseInstance.getExerciseTitle()),
                                trainingExerciseInstance.getOrderNumber(),
                                i + 1,
                                accountId,
                                trainingExerciseInstance.getDayOfWeek())));
            }

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

    private void generateTrainingExerciseSets(Context context) {
        trainingExerciseSetsObj = new TrainingExerciseSetsObj(trainingExerciseInstance.getExerciseTitle());

        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
//        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

//        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        Cursor cursor = db.rawQuery("SELECT RepsNum, Timer, SetNumber FROM TrainingExercise WHERE AccountId = " + accountId + " AND TrainingId = " + trainingExerciseInstance.getTrainingId() + " AND DayOfWeek = " + trainingExerciseInstance.getDayOfWeek() + " AND OrderNumber = " + trainingExerciseInstance.getOrderNumber() + " ORDER BY SetNumber;", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ExerciseSetResult exerciseSetResult = new ExerciseSetResult(0, cursor.getInt(0), cursor.getInt(1));
                trainingExerciseSetsObj.addExerciseSetResult(exerciseSetResult);
            } while (cursor.moveToNext());
        }
    }
}
