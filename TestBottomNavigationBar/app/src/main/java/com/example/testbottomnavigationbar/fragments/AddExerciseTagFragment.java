package com.example.testbottomnavigationbar.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.testbottomnavigationbar.listeners.NewExerciseTagsLogic;

public class AddExerciseTagFragment extends Fragment implements View.OnClickListener {
    NewExerciseTagsLogic newExerciseTagsLogic;

    public AddExerciseTagFragment() {
        super(R.layout.fragment_add_exercise_tag);
        newExerciseTagsLogic = new NewExerciseTagsLogic();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_add_exercise_tag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        handleFilterTypes();
        handleDoneButton();
        tuneToolbar(view);
    }

    private void handleDoneButton() {
        getView().findViewById(R.id.apply).setOnClickListener(newExerciseTagsLogic);
    }

    private void handleFilterTypes() {
        ConstraintLayout constraintLayout = getView().findViewById(R.id.tag_cl);
        setCLChildsListener(constraintLayout);
    }

    private void setCLChildsListener(ConstraintLayout constraintLayout) {
        for (int i = 3; i < constraintLayout.getChildCount() - 1; ++i) {
            constraintLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            if (((TextView) v).getCurrentTextColor() == Color.WHITE) {
                newExerciseTagsLogic.addTag((String) ((TextView) v).getText());
                v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_select_type_background));
                ((TextView) v).setTextColor(Color.BLACK);

                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Add tag");
                }
            } else {
                newExerciseTagsLogic.removeTag((String) ((TextView) v).getText());
                v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_type_background));
                ((TextView) v).setTextColor(Color.WHITE);

                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Remove tag");
                }
            }
        }
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_tag_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
