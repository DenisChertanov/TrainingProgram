package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.db.ExerciseOverview;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.fragments.SearchFragment;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FilterApplyLogic implements View.OnClickListener {
    private HashSet<String> filters;
    private int type;
    private final TrainingDayOfWeekHelper trainingDayOfWeekHelper;

    public FilterApplyLogic(int type, TrainingDayOfWeekHelper trainingDayOfWeekHelper) {
        filters = new HashSet<>();
        this.type = type;
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
    }

    public void addFilter(String filter) {
        filters.add(filter);
    }

    public void removeFilter(String filter) {
        filters.remove(filter);
    }

    @Override
    public void onClick(View v) {
        if (MainActivity.LOG) {
            String res = "";
            for (String filter : filters) {
                res += filter + " ";
            }

            Log.d(MainActivity.TEG, res);
        }

        if (type == 0) {
            Fragment fragment = new SearchFragment(generateExerciseOverviewList(v.getContext()), 0, trainingDayOfWeekHelper);
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragment_cv, fragment, "searchFragment").commit();
            fTrans.addToBackStack(null);
        } else {
            ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();

            Fragment fragment = new SearchFragment(generateExerciseOverviewList(v.getContext()), 1, trainingDayOfWeekHelper);
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragment_cv, fragment).commit();
            fTrans.addToBackStack(null);
        }
    }

    private List<ExerciseOverview> generateExerciseOverviewList(Context context) {
        if (filters.size() == 0) {
            return SearchFragment.generateExerciseOverviewList(context);
        }

        List<ExerciseOverview> exerciseOverviewList = new ArrayList<>();
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        List<Integer> selectedMusclesId = new ArrayList<>();
        Cursor cursor = db.rawQuery(getSQLQueryWithAllFilterMuscleId(), null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                selectedMusclesId.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        cursor = db.rawQuery(getSQLQueryWithAllNeededExercisesId(selectedMusclesId), null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int exerciseId = cursor.getInt(0);

                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "exerciseId " + exerciseId);
                }

                String title;
                Cursor titleCursor = db.rawQuery("SELECT Title FROM Exercise WHERE ExerciseId = " + exerciseId + ";", null);
                titleCursor.moveToFirst();
                title = titleCursor.getString(0);

                ArrayList<String> tags = new ArrayList<>();
                Cursor tagsId = db.rawQuery("SELECT TagId FROM ExerciseToTag WHERE ExerciseId = " + exerciseId + ";", null);
                while (tagsId.moveToNext()) {
                    int tagId = tagsId.getInt(0);

                    Cursor tagTitle = db.rawQuery("SELECT Title FROM ExerciseTag WHERE TagId = " + tagId + ";", null);
                    while (tagTitle.moveToNext()) {
                        tags.add(tagTitle.getString(0));
                    }
                }

                exerciseOverviewList.add(new ExerciseOverview(title, tags));
            } while (cursor.moveToNext());
        }

        return exerciseOverviewList;
    }

    private String getSQLQueryWithAllNeededExercisesId(List<Integer> selectedMusclesId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ExerciseId FROM TargetMuscle WHERE MuscleId IN (");
        for (Integer muscleId : selectedMusclesId) {
            query.append(muscleId);
            query.append(", ");
        }
        query.delete(query.length() - 2, query.length());
        query.append(") GROUP BY ExerciseId HAVING COUNT(*) = ");
        query.append(selectedMusclesId.size());
        query.append(";");

        return query.toString();
    }

    private String getSQLQueryWithAllFilterMuscleId() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT MuscleId FROM Muscle WHERE Title IN (");
        for (String filter : filters) {
            query.append("'");
            query.append(filter);
            query.append("', ");
        }
        query.delete(query.length() - 2, query.length());
        query.append(");");

        return query.toString();
    }
}
