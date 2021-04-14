package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.fragments.ExerciseSetEditingFragment;

public class ExerciseSetLongClickLogic implements View.OnLongClickListener {
    private final int type;
    private final ExerciseInTrainingEditing exerciseInTrainingEditing;

    public ExerciseSetLongClickLogic(int type, ExerciseInTrainingEditing exerciseInTrainingEditing) {
        this.type = type;
        this.exerciseInTrainingEditing = exerciseInTrainingEditing;
    }

    @Override
    public boolean onLongClick(View v) {
        Fragment fragment = new ExerciseSetEditingFragment(type, exerciseInTrainingEditing);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);

        return true;
    }
}
