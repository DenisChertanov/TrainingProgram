package com.example.testbottomnavigationbar.listeners;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.fragments.AddExerciseDescriptionFragment;
import com.example.testbottomnavigationbar.fragments.AddExerciseVideoFragment;

import java.util.HashSet;

public class NewExerciseDescriptionLogic implements View.OnClickListener {
    private HashSet<String> tags;
    private HashSet<String> filters;
    private String title;
    private String description;

    public NewExerciseDescriptionLogic(HashSet<String> tags, HashSet<String> filters, String title) {
        this.tags = tags;
        this.filters = filters;
        this.title = title;
    }

    @Override
    public void onClick(View v) {
        findDescription(v);
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "description   " + description);
        }

        Fragment fragment = new AddExerciseVideoFragment(tags, filters, title, description);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }

    private void findDescription(View view) {
        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_description_et);
        description = editText.getText().toString();
    }
}
