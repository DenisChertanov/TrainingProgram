package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.fragments.AddSetFragment;

public class TrainingExerciseSuccessCreateSetLogic implements View.OnClickListener {
    private final int trainingId;
    private final int dayOfWeek;
    private final int orderNumber;
    private final int exerciseId;
    private final int setNumber;
    private final int type;

    public TrainingExerciseSuccessCreateSetLogic(int trainingId, int dayOfWeek, int orderNumber, int exerciseId, int setNumber, int type) {
        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.type = type;
    }


    @Override
    public void onClick(View v) {
        Fragment fragment = new AddSetFragment(trainingId, dayOfWeek, orderNumber, exerciseId, setNumber, type);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
