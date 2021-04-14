package com.example.testbottomnavigationbar.listeners;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.fragments.AddExerciseDescriptionFragment;
import com.example.testbottomnavigationbar.fragments.AddExerciseMuscleFragment;

import java.util.HashSet;

public class NewExerciseTagsLogic implements View.OnClickListener {
    private HashSet<String> tags;

    public NewExerciseTagsLogic() {
        tags = new HashSet<>();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public void onClick(View v) {
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "tags   " + tags.size());
        }

        if (tags.size() > 3) {
            return;
        }

        Fragment fragment = new AddExerciseMuscleFragment(tags);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
