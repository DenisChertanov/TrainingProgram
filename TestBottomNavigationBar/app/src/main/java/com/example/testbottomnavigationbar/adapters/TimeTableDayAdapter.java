package com.example.testbottomnavigationbar.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.db.TimeTableDay;
import com.example.testbottomnavigationbar.db.TimeTableExercise;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.listeners.AddExerciseInTrainingLogic;
import com.example.testbottomnavigationbar.listeners.OpenEditingTrainingExeciseFragmentLogic;
import com.example.testbottomnavigationbar.listeners.TimeTableExerciseClickLogic;
import com.example.testbottomnavigationbar.listeners.TrainingExerciseSetClickLogic;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.ArrayList;
import java.util.List;

public class TimeTableDayAdapter extends RecyclerView.Adapter<TimeTableDayAdapter.TimeTableDayViewHolder> {
    private List<TimeTableDay> days;
    private final int type;
    private final int accountId;
    private final Training training;

    public TimeTableDayAdapter(List<TimeTableDay> days, int type, int accountId, Training training) {
        this.type = type;
        this.accountId = accountId;
        this.days = new ArrayList<>();
        this.training = training;

        if (type == 0) {
            for (TimeTableDay timeTableDay : days) {
                if (timeTableDay.getExercises().size() != 0) {
                    this.days.add(timeTableDay);
                }
            }
        } else {
            this.days = days;
        }
    }

    @NonNull
    @Override
    public TimeTableDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_layout, parent, false);

        if (type != 1) {
            // hide add exercise button
            ConstraintLayout constraintLayout = (ConstraintLayout) ((ConstraintLayout) view).getChildAt(2);
            constraintLayout.setVisibility(View.GONE);
        }

        return new TimeTableDayAdapter.TimeTableDayViewHolder(view, type, accountId, training);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableDayViewHolder holder, int position) {
        holder.bind(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static final class TimeTableDayViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayOfWeek;
        private final LinearLayout exerciseLinearLayout;
        private final ConstraintLayout addExerciseCL;
        private final Training training;
        private final int type;
        private final int accountId;

        public TimeTableDayViewHolder(@NonNull View itemView, int type, int accountId, Training training) {
            super(itemView);
            this.type = type;
            this.accountId = accountId;
            this.training = training;

            dayOfWeek = itemView.findViewById(R.id.timetable_layout_dayofweek);
            exerciseLinearLayout = itemView.findViewById(R.id.timetable_Layout_linearlayout);
            addExerciseCL = itemView.findViewById(R.id.timetable_layout_add_exercise_cl);
        }

        private void bind(@NonNull TimeTableDay timeTableDay) {
            dayOfWeek.setText(timeTableDay.getStringDayOfWeek());

            exerciseLinearLayout.removeAllViews();
            List<TimeTableExercise> exercises = timeTableDay.getExercises();
            for (TimeTableExercise exercise : exercises) {
                ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(exerciseLinearLayout.getContext()).inflate(R.layout.timetable_exercise, exerciseLinearLayout, false);
                TextView textView = (TextView) constraintLayout.getChildAt(0);
                textView.setText(exercise.getExerciseTitle());

                if (exercise.isLastExercise()) {
                    TextView bottom_line = (TextView) constraintLayout.getChildAt(2);
                    bottom_line.setVisibility(View.GONE);
                }

                exerciseLinearLayout.addView(constraintLayout);

                if (type == 0) {
                    constraintLayout.setOnClickListener(new TimeTableExerciseClickLogic(new TrainingExerciseInstance(exercise.getExerciseTitle(), timeTableDay.getTrainingId(), timeTableDay.getDayOfWeek(), exercise.getOrderNumber())));
                } else {
                    constraintLayout.setOnClickListener(new TrainingExerciseSetClickLogic(type, accountId, new TrainingExerciseInstance(exercise.getExerciseTitle(), timeTableDay.getTrainingId(), timeTableDay.getDayOfWeek(), exercise.getOrderNumber())));
                }

                if (type != 2) {
                    constraintLayout.setOnLongClickListener(new OpenEditingTrainingExeciseFragmentLogic(type, new TrainingExerciseInstance(exercise.getExerciseTitle(), timeTableDay.getTrainingId(), timeTableDay.getDayOfWeek(), exercise.getOrderNumber())));
                }
            }

            // open search fragment
            addExerciseCL.setOnClickListener(new AddExerciseInTrainingLogic(new TrainingDayOfWeekHelper(training.getTrainingId(), timeTableDay.getDayOfWeek())));
        }
    }
}
