package com.example.testbottomnavigationbar.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.listeners.NewExerciseDescriptionLogic;
import com.example.testbottomnavigationbar.listeners.NewExerciseVideoLogic;

import java.util.HashSet;

public class AddExerciseVideoFragment extends Fragment {
    private HashSet<String> tags;
    private HashSet<String> filters;
    private String title;
    private String description;

    public AddExerciseVideoFragment(HashSet<String> tags, HashSet<String> filters, String title, String description) {
        super(R.layout.fragment_add_exercise_videopath);
        this.tags = tags;
        this.filters = filters;
        this.title = title;
        this.description = description;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_add_exercise_videopath, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneToolbar(view);
        tuneDone();

        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_video_et);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void tuneDone() {
        getView().findViewById(R.id.done).setOnClickListener(new NewExerciseVideoLogic(tags, filters, title, description));
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_video_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
