package com.example.testbottomnavigationbar.fragments;

import android.graphics.Color;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.listeners.NewExerciseFilterLogic;
import com.example.testbottomnavigationbar.remote_db.Exercise;
import com.example.testbottomnavigationbar.remote_db.TargetMuscle;

import java.util.HashSet;
import java.util.List;

public class AddExerciseMuscleFragment extends Fragment implements View.OnClickListener {
    NewExerciseFilterLogic newExerciseFilterLogic;

    public AddExerciseMuscleFragment(HashSet<String> tags) {
        super(R.layout.fragment_add_exercise_muscle);
        newExerciseFilterLogic = new NewExerciseFilterLogic(tags);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_add_exercise_muscle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        handleFilterTypes();
        handleDoneButton();
        tuneToolbar(view);
        tuneEditText(view);
    }

    private void tuneEditText(View view) {
        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_title_et);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void handleDoneButton() {
        getView().findViewById(R.id.apply).setOnClickListener(newExerciseFilterLogic);
    }

    private void handleFilterTypes() {
//        ConstraintLayout constraintLayout = getView().findViewById(R.id.training_type_cl);
//        setCLChildsListener(constraintLayout);

        ConstraintLayout constraintLayout = getView().findViewById(R.id.chest_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.biceps_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.triceps_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.back_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.shoulders_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.hip_biceps_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.thigh_quads_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.press_cl);
        setCLChildsListener(constraintLayout);
    }

    private void setCLChildsListener(ConstraintLayout constraintLayout) {
        for (int i = 1; i < constraintLayout.getChildCount() - 1; ++i) {
            constraintLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            if (((TextView) v).getText().equals("Все")) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Все  " + ((ConstraintLayout) v.getParent()).getChildCount());
                }

                ConstraintLayout constraintLayout = (ConstraintLayout) v.getParent();
                for (int i = 2; i < constraintLayout.getChildCount() - 1; ++i) {
                    TextView textView = (TextView) constraintLayout.getChildAt(i);

                    if (textView.getCurrentTextColor() == Color.WHITE) {
                        newExerciseFilterLogic.addFilter((String) textView.getText());
                        textView.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_select_type_background));
                        textView.setTextColor(Color.BLACK);
                    }
                }
            } else {
                if (((TextView) v).getCurrentTextColor() == Color.WHITE) {
                    newExerciseFilterLogic.addFilter((String) ((TextView) v).getText());
                    v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_select_type_background));
                    ((TextView) v).setTextColor(Color.BLACK);

                    if (MainActivity.LOG) {
                        Log.d(MainActivity.TEG, "Add filter");
                    }
                } else {
                    newExerciseFilterLogic.removeFilter((String) ((TextView) v).getText());
                    v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_type_background));
                    ((TextView) v).setTextColor(Color.WHITE);

                    if (MainActivity.LOG) {
                        Log.d(MainActivity.TEG, "Remove filter");
                    }
                }
            }
        }
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_muscle_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
