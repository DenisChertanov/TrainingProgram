package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.adapters.TimeTableDayAdapter;
import com.example.testbottomnavigationbar.db.ExerciseTemp;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.remote_db.Exercise;
import com.example.testbottomnavigationbar.db.TimeTableDay;
import com.example.testbottomnavigationbar.db.TimeTableExercise;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TimeTableFragment extends Fragment {
    private final int trainingId;
    private final int type;
    private final int accountId;
    private final Training training;

    public TimeTableFragment(int trainingId, int type, int accountId, Training training) {
        super(R.layout.fragment_timetable);
        this.trainingId = trainingId;
        this.type = type;
        this.accountId = accountId;
        this.training = training;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    private void fillRecyclerView(List<TimeTableDay> days) {
        RecyclerView recyclerView = getView().findViewById(R.id.fragment_timetable_recyclerview);
        TimeTableDayAdapter timeTableDayAdapter = new TimeTableDayAdapter(days, type, accountId, training);
        recyclerView.setAdapter(timeTableDayAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (type != 0) {
            handleType(view);
        }

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "TimeTableFragment creating");
        }

        fillRecyclerView(generateTimeTableDayList(view.getContext()));
    }

    private void handleType(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_timetable_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private List<TimeTableDay> generateTimeTableDayList(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
//        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        ArrayList<TimeTableDay> list = new ArrayList<>();

//        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "out " + accountId + " " + trainingId);
        }

        if (trainingId != -1) {
            TreeMap<Integer, ArrayList<ExerciseTemp>> map = new TreeMap<>();
            Cursor cursor = null;

            if (type == 0) {
                cursor = db.rawQuery("SELECT DISTINCT DayOfWeek, OrderNumber, ExerciseId FROM TrainingExerciseSuccess WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + ";", null);
            } else {
                cursor = db.rawQuery("SELECT DISTINCT DayOfWeek, OrderNumber, ExerciseId FROM TrainingExercise WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + ";", null);
            }

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int dayOfWeek = cursor.getInt(0);
                    int orderNumber = cursor.getInt(1);
                    String title = getExerciseTitleFromId(db, cursor.getInt(2));

                    if (!map.containsKey(dayOfWeek)) {
                        map.put(dayOfWeek, new ArrayList<>());
                    }
                    ArrayList<ExerciseTemp> exercises = map.get(dayOfWeek);
                    exercises.add(new ExerciseTemp(title, orderNumber));
                    map.put(dayOfWeek, exercises);
                } while (cursor.moveToNext());
            }


            for (int i = 0; i < 7; ++i) {
                if (!map.containsKey(i)) {
                    map.put(i, new ArrayList<>());
                }
            }

            for (Integer key : map.keySet()) {
                ArrayList<ExerciseTemp> exercises = map.get(key);
                exercises.sort((o1, o2) -> (o1.getOrderNumber() < o2.getOrderNumber() ? -1 : 1));

                ArrayList<TimeTableExercise> texercises = new ArrayList<>();
                for (int i = 0; i < exercises.size(); ++i) {
                    texercises.add(new TimeTableExercise(exercises.get(i).getTitle(), (i == exercises.size() - 1), exercises.get(i).getOrderNumber()));
                }

                list.add(new TimeTableDay(trainingId, key, texercises));
            }
        }

        return list;
    }

    private String getExerciseTitleFromId(SQLiteDatabase db, int exerciseId) {
        Cursor cursor = db.rawQuery("SELECT Title FROM Exercise WHERE ExerciseId = " + exerciseId + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            return null;
        }
    }
}
