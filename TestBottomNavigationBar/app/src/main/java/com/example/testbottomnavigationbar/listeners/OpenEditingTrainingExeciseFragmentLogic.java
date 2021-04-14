package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.fragments.EditingTrainingExerciseFragment;

public class OpenEditingTrainingExeciseFragmentLogic implements View.OnLongClickListener {
    private final int type;
    private final TrainingExerciseInstance trainingExerciseInstance;

    public OpenEditingTrainingExeciseFragmentLogic(int type, TrainingExerciseInstance trainingExerciseInstance) {
        this.type = type;
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public boolean onLongClick(View v) {
        Fragment fragment = new EditingTrainingExerciseFragment(type, trainingExerciseInstance);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);

        return true;
    }
}
