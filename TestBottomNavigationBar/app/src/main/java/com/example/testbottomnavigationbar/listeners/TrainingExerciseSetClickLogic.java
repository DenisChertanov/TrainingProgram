package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.fragments.TrainingExerciseSetsFragment;
import com.example.testbottomnavigationbar.fragments.TrainingExerciseSuccessFragment;

public class TrainingExerciseSetClickLogic implements View.OnClickListener {
    private final int type;
    private final int accountId;
    private final TrainingExerciseInstance trainingExerciseInstance;

    public TrainingExerciseSetClickLogic(int type, int accountId, TrainingExerciseInstance trainingExerciseInstance) {
        this.type = type;
        this.accountId = accountId;
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new TrainingExerciseSetsFragment(type, accountId, trainingExerciseInstance);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
