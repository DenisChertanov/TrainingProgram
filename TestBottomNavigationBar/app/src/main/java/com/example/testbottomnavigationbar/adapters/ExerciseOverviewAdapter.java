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
import com.example.testbottomnavigationbar.db.ExerciseOverview;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.fragments.ExerciseDescriptionFragment;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.ArrayList;
import java.util.List;

public class ExerciseOverviewAdapter extends RecyclerView.Adapter<ExerciseOverviewAdapter.ExerciseOverviewViewHolder> implements View.OnClickListener {
    private final List<ExerciseOverview> exercises;
    private final int type;
    TrainingDayOfWeekHelper trainingDayOfWeekHelper;

    public ExerciseOverviewAdapter(List<ExerciseOverview> exercises, int type, TrainingDayOfWeekHelper trainingDayOfWeekHelper) {
        this.exercises = exercises;
        this.type = type;
        this.trainingDayOfWeekHelper = trainingDayOfWeekHelper;
    }

    @NonNull
    @Override
    public ExerciseOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_overview, viewGroup, false);
        view.setOnClickListener(this);
        return new ExerciseOverviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseOverviewViewHolder viewHolder, int i) {
        viewHolder.bind(exercises.get(i));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public void onClick(View v) {
        ConstraintLayout constraintLayout = (ConstraintLayout) v;
        TextView textView = (TextView) constraintLayout.getChildAt(0);
        String exerciseTitle = (String) textView.getText();

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, exerciseTitle);
        }

        Fragment fragment = new ExerciseDescriptionFragment(exerciseTitle, type, trainingDayOfWeekHelper);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }

    static final class ExerciseOverviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final LinearLayout tagsLinearLayout;

        public ExerciseOverviewViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.exercise_title_textview);
            tagsLinearLayout = itemView.findViewById(R.id.exercise_title_linearlayout);
        }

        private void bind(@NonNull ExerciseOverview exerciseOverview) {
            titleTextView.setText(exerciseOverview.getTitle());

            tagsLinearLayout.removeAllViews();
            ArrayList<String> tags = exerciseOverview.getTags();
            for (String tag : tags) {
                ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(tagsLinearLayout.getContext()).inflate(R.layout.tag_layout, tagsLinearLayout, false);
                TextView textView = (TextView) constraintLayout.getChildAt(0);
                textView.setText(tag);

                tagsLinearLayout.addView(constraintLayout);
            }
        }
    }
}
