package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.listeners.AddSetSaveLogic;
import com.example.testbottomnavigationbar.listeners.SetEditLogic;
import com.example.testbottomnavigationbar.listeners.SetRemoveLogic;

public class ExerciseSetEditingFragment extends Fragment {
    private final int type;
    private final ExerciseInTrainingEditing exerciseInTrainingEditing;

    public ExerciseSetEditingFragment(int type, ExerciseInTrainingEditing exerciseInTrainingEditing) {
        super(R.layout.fragment_exercise_set_editing);
        this.type = type;
        this.exerciseInTrainingEditing = exerciseInTrainingEditing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_exercise_set_editing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneToolbar(view);
        tuneEditButton(view);
        tuneRemoveButton(view);

        tuneEditText(((FragmentActivity) view.getContext()).findViewById(R.id.add_set_layout_weight_et));
        tuneEditText(((FragmentActivity) view.getContext()).findViewById(R.id.add_set_layout_count_et));
        tuneEditText(((FragmentActivity) view.getContext()).findViewById(R.id.add_set_layout_timer_et));

        if (type == 1) {
            // hide weight
            getView().findViewById(R.id.add_set_layout_weight_ic).setVisibility(View.GONE);
            getView().findViewById(R.id.add_set_layout_weight_et).setVisibility(View.GONE);
            getView().findViewById(R.id.add_set_layout_weight_tv).setVisibility(View.GONE);
        }
    }

    private void tuneEditText(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void tuneEditButton(View view) {
        TextView textView = ((FragmentActivity)view.getContext()).findViewById(R.id.add_set_layout_save);
        textView.setOnClickListener(new SetEditLogic(type, exerciseInTrainingEditing));
    }

    private void tuneRemoveButton(View view) {
        TextView textView = ((FragmentActivity)view.getContext()).findViewById(R.id.add_set_layout_delete);

        if (!needToShow(view)) {
            textView.setVisibility(View.GONE);
        }

        textView.setOnClickListener(new SetRemoveLogic(type, exerciseInTrainingEditing));
    }

    private boolean needToShow(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        int maxSetNumber = -1;

        if (type == 0) {
            maxSetNumber = SQLiteHelper.getMaxSetNumberFromTrainingExerciseSuccess(db,
                    exerciseInTrainingEditing.getAccountId(), exerciseInTrainingEditing.getTrainingId(),
                    exerciseInTrainingEditing.getDayOfWeek(), exerciseInTrainingEditing.getOrderNumber());
        }
        else {
            maxSetNumber = SQLiteHelper.getMaxSetNumberFromTrainingExercise(db,
                    exerciseInTrainingEditing.getAccountId(), exerciseInTrainingEditing.getTrainingId(),
                    exerciseInTrainingEditing.getDayOfWeek(), exerciseInTrainingEditing.getOrderNumber());
        }

        return (maxSetNumber == exerciseInTrainingEditing.getSetNumber());
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_exercise_set_editing_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
