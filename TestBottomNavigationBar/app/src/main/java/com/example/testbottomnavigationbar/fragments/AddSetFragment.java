package com.example.testbottomnavigationbar.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.listeners.AddSetSaveLogic;

public class AddSetFragment extends Fragment {
    private final int trainingId;
    private final int dayOfWeek;
    private final int orderNumber;
    private final int exerciseId;
    private final int setNumber;
    private final int type;

    public AddSetFragment(int trainingId, int dayOfWeek, int orderNumber, int exerciseId, int setNumber, int type) {
        super(R.layout.add_set_layout);

        this.trainingId = trainingId;
        this.dayOfWeek = dayOfWeek;
        this.orderNumber = orderNumber;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.add_set_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (type == 1) {
            hideWeight(view);
        }

        tuneET();
        tuneToolbar(view);
        tuneSaveButton(view);
    }

    private void tuneET() {
        EditText editText = getView().findViewById(R.id.add_set_layout_weight_et);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });

        editText = getView().findViewById(R.id.add_set_layout_count_et);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });

        editText = getView().findViewById(R.id.add_set_layout_timer_et);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void hideWeight(View view) {
        ImageView imageView = ((FragmentActivity) view.getContext()).findViewById(R.id.add_set_layout_weight_ic);
        imageView.setVisibility(View.INVISIBLE);

        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.add_set_layout_weight_et);
        editText.setVisibility(View.INVISIBLE);

        TextView textView = ((FragmentActivity) view.getContext()).findViewById(R.id.add_set_layout_weight_tv);
        textView.setVisibility(View.INVISIBLE);
    }

    private void tuneSaveButton(View view) {
        TextView textView = ((FragmentActivity)view.getContext()).findViewById(R.id.add_set_layout_save);
        textView.setOnClickListener(new AddSetSaveLogic(trainingId, dayOfWeek, orderNumber, exerciseId, setNumber, type));
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.add_set_layout_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
