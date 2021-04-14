package com.example.testbottomnavigationbar.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.listeners.FilterApplyLogic;
import com.example.testbottomnavigationbar.listeners.FilterCancelLogic;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.remote_db.Training;

public class FilterFragment extends Fragment implements View.OnClickListener {
    private FilterApplyLogic filterLogic;
    private int type;
    private final TrainingDayOfWeekHelper trainingDayOfWeekHelper;

    public FilterFragment(int type, TrainingDayOfWeekHelper trainingDayOfWeekHelper) {
        super(R.layout.filters_layout);
        this.type = type;
        filterLogic = new FilterApplyLogic(type, trainingDayOfWeekHelper);
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.filters_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        handleCancelButton();
        handleFilterTypes();
        handleApplyButton();
    }

    private void handleApplyButton() {
        getView().findViewById(R.id.apply).setOnClickListener(filterLogic);
    }

    private void handleCancelButton() {
        getView().findViewById(R.id.filter_layout_cancel).setOnClickListener(new FilterCancelLogic(type, trainingDayOfWeekHelper));
    }

    private void handleFilterTypes() {
//        ConstraintLayout constraintLayout = getView().findViewById(R.id.training_type_cl);
//        setCLChildsListener(constraintLayout);

        ConstraintLayout constraintLayout = getView().findViewById(R.id.chest_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.biceps_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.triceps_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.back_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.shoulders_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.hip_biceps_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.thigh_quads_cl);
        setCLChildsListener(constraintLayout);

        constraintLayout = getView().findViewById(R.id.press_cl);
        setCLChildsListener(constraintLayout);
    }

    private void setCLChildsListener(ConstraintLayout constraintLayout) {
        for (int i = 1; i < constraintLayout.getChildCount() - 1; ++i) {
            constraintLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            if (((TextView) v).getText().equals("Все")) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Все  " + ((ConstraintLayout) v.getParent()).getChildCount());
                }

                ConstraintLayout constraintLayout = (ConstraintLayout) v.getParent();
                for (int i = 2; i < constraintLayout.getChildCount() - 1; ++i) {
                    TextView textView = (TextView) constraintLayout.getChildAt(i);

                    if (textView.getCurrentTextColor() == Color.WHITE) {
                        filterLogic.addFilter((String) textView.getText());
                        textView.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_select_type_background));
                        textView.setTextColor(Color.BLACK);
                    }
                }
            } else {
                if (((TextView) v).getCurrentTextColor() == Color.WHITE) {
                    filterLogic.addFilter((String) ((TextView) v).getText());
                    v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_select_type_background));
                    ((TextView) v).setTextColor(Color.BLACK);

                    if (MainActivity.LOG) {
                        Log.d(MainActivity.TEG, "Add filter");
                    }
                } else {
                    filterLogic.removeFilter((String) ((TextView) v).getText());
                    v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.filter_type_background));
                    ((TextView) v).setTextColor(Color.WHITE);

                    if (MainActivity.LOG) {
                        Log.d(MainActivity.TEG, "Remove filter");
                    }
                }
            }
        }
    }
}
