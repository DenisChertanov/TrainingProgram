package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.SearchFragment;
import com.example.testbottomnavigationbar.fragments.TrainingExerciseSuccessFragment;
import com.example.testbottomnavigationbar.remote_db.Exercise;
import com.example.testbottomnavigationbar.remote_db.ExerciseTag;
import com.example.testbottomnavigationbar.remote_db.ExerciseToTag;
import com.example.testbottomnavigationbar.remote_db.TargetMuscle;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertNewExerciseTask;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertTrainingExerciseSuccessTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NewExerciseVideoLogic implements View.OnClickListener {
    private HashSet<String> tags;
    private HashSet<String> filters;
    private String title;
    private String description;
    private String videoPath;

    public NewExerciseVideoLogic(HashSet<String> tags, HashSet<String> filters, String title, String description) {
        this.tags = tags;
        this.filters = filters;
        this.title = title;
        this.description = description;
    }

    @Override
    public void onClick(View v) {
        findVideoPath(v);
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "tags   " + tags.size());
            Log.d(MainActivity.TEG, "video   " + videoPath);
            Log.d(MainActivity.TEG, "description   " + description);
            Log.d(MainActivity.TEG, "title   " + title);
            Log.d(MainActivity.TEG, "filters   " + filters.size());
        }

        addInDB(v.getContext());

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate("mainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment fragment = new SearchFragment(SearchFragment.generateExerciseOverviewList(v.getContext()), 0, null);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment, "searchFragment").commit();
        fTrans.addToBackStack(null);
    }

    private void findVideoPath(View view) {
        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_video_et);
        videoPath = editText.getText().toString();
    }

    private void addInDB(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
                "VALUES ('" + title + "', '" + description + "', '" + videoPath + "');");

        int exerciseId = SQLiteHelper.getExerciseIdFromTitle(db, title);
        List<ExerciseToTag> exerciseToTagList = new ArrayList<>();
        List<TargetMuscle> targetMuscleList = new ArrayList<>();

        for (String tag : tags) {
            ExerciseToTag exerciseToTag = new ExerciseToTag(exerciseId, SQLiteHelper.getTagIdFromTitle(db, tag));
            exerciseToTagList.add(exerciseToTag);

            db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
                    "VALUES ('" + exerciseId + "', '" + exerciseToTag.getTagId() + "');");
        }

        for (String filter : filters) {
            TargetMuscle targetMuscle = new TargetMuscle(exerciseId, SQLiteHelper.getMuscleIdFromTitle(db, filter));
            targetMuscleList.add(targetMuscle);

            db.execSQL("INSERT INTO TargetMuscle(ExerciseId, MuscleId)\n" +
                    "VALUES ('" + exerciseId + "', '" + targetMuscle.getMuscleId() + "');");
        }

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "new exerciseId   " + SQLiteHelper.getExerciseIdFromTitle(db, title));
        }


        addInRemoteDB(exerciseId, exerciseToTagList, targetMuscleList);
    }

    private void addInRemoteDB(int exerciseId, List<ExerciseToTag> exerciseTagList, List<TargetMuscle> targetMuscleList) {
        new InsertNewExerciseTask(new Exercise(exerciseId, title, description, videoPath), exerciseTagList, targetMuscleList).execute();
    }
}
