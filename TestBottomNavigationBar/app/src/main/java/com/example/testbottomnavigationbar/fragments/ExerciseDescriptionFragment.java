package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.listeners.AddExerciseFromDescriptionLogic;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDescriptionFragment extends Fragment {
    private final String exerciseTitle;
    private int type;
    private TrainingDayOfWeekHelper trainingDayOfWeekHelper;

    public ExerciseDescriptionFragment(String title, int type, TrainingDayOfWeekHelper trainingDayOfWeekHelper) {
        super(R.layout.fragment_exercise_description);
        exerciseTitle = title;
        this.type = type;
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_exercise_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneToolbar(view);
        addContent(view.getContext());

        if (type == 1) {
            handleType();
        }
    }

    private void handleType() {
        ImageView imageView = getView().findViewById(R.id.fragment_exercise_description_add);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new AddExerciseFromDescriptionLogic(trainingDayOfWeekHelper, exerciseTitle));
    }

    private void addContent(Context context) {
        String description = "", videoPath = "";
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM Exercise WHERE Title = '" + exerciseTitle + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            description = cursor.getString(2);
            videoPath = cursor.getString(3);
        }

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "videoPath   " + videoPath);
        }

        TextView title = ((FragmentActivity) context).findViewById(R.id.fragment_exercise_description_title);
        title.setText(exerciseTitle);

        TextView textDescription = ((FragmentActivity) context).findViewById(R.id.fragment_exercise_description_description);
        textDescription.setText(description);

        TextView textVideoPath = ((FragmentActivity) context).findViewById(R.id.fragment_exercise_description_video);
        textVideoPath.setText(videoPath);

        TextView textTargetMuscles = ((FragmentActivity) context).findViewById(R.id.fragment_exercise_description_target_muscles);
        List<String> targetMuscles = getExerciseMuscles(db, SQLiteHelper.getExerciseIdFromTitle(db, exerciseTitle));
        for (String targetMuscle : targetMuscles) {
            String text = (String) textTargetMuscles.getText();
            if (text.length() != 0) {
                text += "\n";
            }
            text += "- " + targetMuscle;

            textTargetMuscles.setText(text);
        }
    }

    private List<String> getExerciseMuscles(SQLiteDatabase db, int exerciseId) {
        List<String> targetMuscles = new ArrayList<>();

        Cursor muscleIdCursor = db.rawQuery("SELECT MuscleId FROM TargetMuscle WHERE ExerciseId = " + exerciseId + ";", null);
        if (muscleIdCursor != null && muscleIdCursor.moveToFirst()) {
            do {
                int muscleId = muscleIdCursor.getInt(0);

                Cursor muscleTitleCursor = db.rawQuery("SELECT Title FROM Muscle WHERE MuscleId = " + muscleId + ";", null);
                if (muscleTitleCursor != null && muscleTitleCursor.moveToFirst()) {
                    targetMuscles.add(muscleTitleCursor.getString(0));
                }
            } while (muscleIdCursor.moveToNext());
        }

        return targetMuscles;
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.my_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
