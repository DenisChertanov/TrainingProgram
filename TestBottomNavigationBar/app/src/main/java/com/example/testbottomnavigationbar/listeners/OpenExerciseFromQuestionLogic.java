package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.fragments.AddExerciseTagFragment;
import com.example.testbottomnavigationbar.fragments.ExerciseDescriptionFragment;

public class OpenExerciseFromQuestionLogic implements View.OnClickListener {
    private final String title;

    public OpenExerciseFromQuestionLogic(String title) {
        this.title = title;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new ExerciseDescriptionFragment(title, 0, null);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
