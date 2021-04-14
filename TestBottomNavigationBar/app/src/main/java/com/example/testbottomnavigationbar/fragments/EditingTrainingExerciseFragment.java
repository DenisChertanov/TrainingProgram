package com.example.testbottomnavigationbar.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.listeners.DeleteTrainingExerciseLogic;
import com.example.testbottomnavigationbar.listeners.MoveTrainingExerciseLogic;

public class EditingTrainingExerciseFragment extends Fragment {
    private final int type;
    private final TrainingExerciseInstance trainingExerciseInstance;

    public EditingTrainingExerciseFragment(int type, TrainingExerciseInstance trainingExerciseInstance) {
        super(R.layout.fragment_editing_training_exercise);
        this.type = type;
        this.trainingExerciseInstance = trainingExerciseInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_editing_training_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneToolbar(view);
        tuneEditText(getView().findViewById(R.id.fragment_editing_training_exercise_et));
        tuneMoveButton();
        tuneDeleteButton();
    }

    private void tuneEditText(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void tuneMoveButton() {
        TextView moveButton = getView().findViewById(R.id.fragment_training_editing_edit);
        moveButton.setOnClickListener(new MoveTrainingExerciseLogic(type, trainingExerciseInstance));
    }

    private void tuneDeleteButton() {
        TextView deleteButton = getView().findViewById(R.id.fragment_editing_training_exercise_delete);
        deleteButton.setOnClickListener(new DeleteTrainingExerciseLogic(type, trainingExerciseInstance));
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_editing_training_exercise_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
