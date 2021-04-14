package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.TrainingEditingHelper;
import com.example.testbottomnavigationbar.listeners.EditTrainingTitleLogic;
import com.example.testbottomnavigationbar.listeners.RemoveTrainingLogic;
import com.example.testbottomnavigationbar.listeners.SetCurrentTrainingLogic;

public class TrainingEditingFragment extends Fragment {
    private final TrainingEditingHelper trainingEditingHelper;

    public TrainingEditingFragment(TrainingEditingHelper trainingEditingHelper) {
        super(R.layout.fragment_training_editing);

        this.trainingEditingHelper = trainingEditingHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_training_editing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneToolbar(view);
        tuneEditButton(view);
        tuneRemoveButton(view);
        tuneSetCurrentTrainingButton(view);

        tuneEditText(((FragmentActivity) view.getContext()).findViewById(R.id.fragment_training_editing_et));
    }

    private void tuneEditText(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void tuneEditButton(View view) {
        TextView textView = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_training_editing_edit);
        textView.setOnClickListener(new EditTrainingTitleLogic(trainingEditingHelper));
    }

    private void tuneRemoveButton(View view) {
        TextView textView = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_training_editing_delete);
        textView.setOnClickListener(new RemoveTrainingLogic(trainingEditingHelper));
    }

    private void tuneSetCurrentTrainingButton(View view) {
        TextView textView = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_training_editing_current_training);
        textView.setOnClickListener(new SetCurrentTrainingLogic(trainingEditingHelper));
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_training_editing_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
