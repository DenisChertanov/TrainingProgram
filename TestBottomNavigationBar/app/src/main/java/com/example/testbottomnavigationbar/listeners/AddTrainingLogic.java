package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.fragments.CreatingTrainingFragment;

public class AddTrainingLogic implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Fragment fragment = new CreatingTrainingFragment();
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
