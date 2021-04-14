package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.fragments.SearchFragment;
import com.example.testbottomnavigationbar.remote_db.Training;

public class FilterCancelLogic implements View.OnClickListener {
    private int type;
    private final TrainingDayOfWeekHelper trainingDayOfWeekHelper;

    public FilterCancelLogic(int type, TrainingDayOfWeekHelper trainingDayOfWeekHelper) {
        this.type = type;
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
    }

    @Override
    public void onClick(View v) {
        if (type == 0) {
            ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
        } else {
            ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();

            Fragment fragment = new SearchFragment(SearchFragment.generateExerciseOverviewList(v.getContext()), 1, trainingDayOfWeekHelper);
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragment_cv, fragment).commit();
            fTrans.addToBackStack(null);
        }
    }
}
