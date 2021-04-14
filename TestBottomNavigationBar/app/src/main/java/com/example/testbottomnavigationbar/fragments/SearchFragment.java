package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.adapters.ExerciseOverviewAdapter;
import com.example.testbottomnavigationbar.db.ExerciseOverview;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.listeners.AddExerciseSearchLogic;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private List<ExerciseOverview> exerciseOverviewList;
    private int type;
    private final TrainingDayOfWeekHelper trainingDayOfWeekHelper;

    public SearchFragment(List<ExerciseOverview> exerciseOverviewList, int type, TrainingDayOfWeekHelper trainingDayOfWeekHelper) {
        super(R.layout.fragment_search);
        this.exerciseOverviewList = exerciseOverviewList;
        this.type = type;
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private void fillRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.fragment_search_recyclerview);
        ExerciseOverviewAdapter exerciseOverviewAdapter = new ExerciseOverviewAdapter(exerciseOverviewList, type, trainingDayOfWeekHelper);
        recyclerView.setAdapter(exerciseOverviewAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (type == 1) {
            handleType(view);
        }

        fillRecyclerView();
        tuneEditText(view.getContext());
        tuneFilter();
        tuneAddExercise();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ConstraintLayout) {
            if (type == 0) {
                Fragment fragment = new FilterFragment(type, trainingDayOfWeekHelper);
                FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment_cv, fragment).commit();
                fTrans.addToBackStack(null);
            } else {
                ((FragmentActivity) getView().getContext()).getSupportFragmentManager().popBackStack();

                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Start filter");
                }

                Fragment fragment = new FilterFragment(type, trainingDayOfWeekHelper);
                FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment_cv, fragment).commit();
                fTrans.addToBackStack(null);
            }
        }
    }

    private void handleType(View view) {
        tuneToolbar(view);
        hideAddExerciseButton();
    }

    private void hideAddExerciseButton() {
        ConstraintLayout constraintLayout = getView().findViewById(R.id.fragment_search_add_exercise_cl);
        constraintLayout.setVisibility(View.GONE);
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_search_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void tuneAddExercise() {
        ConstraintLayout constraintLayout = getView().findViewById(R.id.fragment_search_add_exercise_cl);
        constraintLayout.setOnClickListener(new AddExerciseSearchLogic());
    }

    private void tuneFilter() {
        ConstraintLayout constraintLayout = getView().findViewById(R.id.fragment_search_filter_cl);
        constraintLayout.setOnClickListener(this);
    }

    private void tuneEditText(Context context) {
        EditText editText = getView().findViewById(R.id.fragment_search_edittext);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "EditText " + s);
                }

                exerciseOverviewList = generateExerciseOverviewListFromTitle(context, s.toString());
                fillRecyclerView();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
//
//        editText.setOnFocusChangeListener((v, hasFocus) -> {
//            if(v.getId() == R.id.fragment_search_edittext && !hasFocus) {
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//        });
    }

    public static List<ExerciseOverview> generateExerciseOverviewListFromTitle(Context context, String prefix) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        List<ExerciseOverview> exerciseOverviewList = new ArrayList<>();

        Cursor exercises = db.rawQuery("SELECT ExerciseId, Title FROM Exercise WHERE Title LIKE '" + prefix + "%';", null);
        while (exercises.moveToNext()) {
            int id = exercises.getInt(0);
            String title = exercises.getString(1);

            ArrayList<String> tags = new ArrayList<>();
            Cursor tagsId = db.rawQuery("SELECT TagId FROM ExerciseToTag WHERE ExerciseId = " + id + ";", null);
            while (tagsId.moveToNext()) {
                int tagId = tagsId.getInt(0);

                Cursor tagTitle = db.rawQuery("SELECT Title FROM ExerciseTag WHERE TagId = " + tagId + ";", null);
                while (tagTitle.moveToNext()) {
                    tags.add(tagTitle.getString(0));
                }
            }

            exerciseOverviewList.add(new ExerciseOverview(title, tags));

            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, title + " " + id + " " + tags.size());
            }
        }

        return exerciseOverviewList;
    }

    public static List<ExerciseOverview> generateExerciseOverviewList(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        List<ExerciseOverview> exerciseOverviewList = new ArrayList<>();

        Cursor exercises = db.rawQuery("SELECT ExerciseId, Title FROM Exercise;", null);
        while (exercises.moveToNext()) {
            int id = exercises.getInt(0);
            String title = exercises.getString(1);

            ArrayList<String> tags = new ArrayList<>();
            Cursor tagsId = db.rawQuery("SELECT TagId FROM ExerciseToTag WHERE ExerciseId = " + id + ";", null);
            while (tagsId.moveToNext()) {
                int tagId = tagsId.getInt(0);

                Cursor tagTitle = db.rawQuery("SELECT Title FROM ExerciseTag WHERE TagId = " + tagId + ";", null);
                while (tagTitle.moveToNext()) {
                    tags.add(tagTitle.getString(0));
                }
            }

            exerciseOverviewList.add(new ExerciseOverview(title, tags));

            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, title + " " + id + " " + tags.size());
            }
        }

        return exerciseOverviewList;
    }
}
