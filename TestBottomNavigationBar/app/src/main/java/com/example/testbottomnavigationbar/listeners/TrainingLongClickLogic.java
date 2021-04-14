package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.TrainingEditingHelper;
import com.example.testbottomnavigationbar.fragments.TrainingEditingFragment;

public class TrainingLongClickLogic implements View.OnLongClickListener {
    private final TrainingEditingHelper trainingEditingHelper;

    public TrainingLongClickLogic(TrainingEditingHelper trainingEditingHelper) {
        this.trainingEditingHelper = trainingEditingHelper;
    }

    @Override
    public boolean onLongClick(View v) {
        Fragment fragment = new TrainingEditingFragment(trainingEditingHelper);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);

        return true;
    }
}
