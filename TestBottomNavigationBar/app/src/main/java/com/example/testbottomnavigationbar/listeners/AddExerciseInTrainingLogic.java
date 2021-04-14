package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.fragments.SearchFragment;

public class AddExerciseInTrainingLogic implements View.OnClickListener {
    private final TrainingDayOfWeekHelper trainingDayOfWeekHelper;

    public AddExerciseInTrainingLogic(TrainingDayOfWeekHelper trainingDayOfWeekHelper) {
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new SearchFragment(SearchFragment.generateExerciseOverviewList(v.getContext()), 1, trainingDayOfWeekHelper);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
