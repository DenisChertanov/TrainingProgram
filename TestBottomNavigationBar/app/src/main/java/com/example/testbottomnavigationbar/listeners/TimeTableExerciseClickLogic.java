package com.example.testbottomnavigationbar.listeners;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.fragments.TrainingExerciseSuccessFragment;

public class TimeTableExerciseClickLogic implements View.OnClickListener {
    private final TrainingExerciseInstance trainingExerciseInstance;

    public TimeTableExerciseClickLogic(TrainingExerciseInstance trainingExerciseInstance) {
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new TrainingExerciseSuccessFragment(trainingExerciseInstance);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
