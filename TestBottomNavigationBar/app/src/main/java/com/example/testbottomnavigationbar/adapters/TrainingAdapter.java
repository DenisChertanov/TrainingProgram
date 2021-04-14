package com.example.testbottomnavigationbar.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.entities.TrainingDayOfWeekHelper;
import com.example.testbottomnavigationbar.entities.TrainingEditingHelper;
import com.example.testbottomnavigationbar.fragments.AddExerciseTagFragment;
import com.example.testbottomnavigationbar.fragments.CopyTrainingFragment;
import com.example.testbottomnavigationbar.fragments.TimeTableFragment;
import com.example.testbottomnavigationbar.listeners.TrainingLongClickLogic;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.List;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder> {
    private final int type;
    private final int accountId;
    private final List<Training> trainings;

    public TrainingAdapter(int type, int accountId, List<Training> trainings) {
        this.type = type;
        this.accountId = accountId;
        this.trainings = trainings;
    }

    @NonNull
    @Override
    public TrainingAdapter.TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.training_overview, viewGroup, false);
        return new TrainingAdapter.TrainingViewHolder(view, type, accountId);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingAdapter.TrainingViewHolder viewHolder, int i) {
        viewHolder.bind(trainings.get(i));
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    static final class TrainingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final int type;
        private final int accountId;
        private final TextView titleTextView;
        private Training training;

        public TrainingViewHolder(@NonNull View itemView, int type, int accountId) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.training_overview_textview);
            this.type = type;
            this.accountId = accountId;
        }

        private void bind(@NonNull Training training) {
            titleTextView.setText(training.getTitle());
            this.training = training;

            ConstraintLayout constraintLayout = (ConstraintLayout) titleTextView.getParent();
            constraintLayout.setOnClickListener(this);

            if (type == 1) {
                constraintLayout.setOnLongClickListener(new TrainingLongClickLogic(new TrainingEditingHelper(training.getTrainingId(), training.getTitle())));
            } else {
                // Сделать копирование тренировки
                constraintLayout.setOnLongClickListener(v -> {
                    Fragment fragment = new CopyTrainingFragment(accountId, training);
                    FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    fTrans.replace(R.id.fragment_cv, fragment).commit();
                    fTrans.addToBackStack(null);
                    return true;
                });
            }
        }

        @Override
        public void onClick(View v) {
            SQLiteDatabase db = v.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

            Fragment fragment = new TimeTableFragment(SQLiteHelper.getTrainingIdFromTitle(db, titleTextView.getText().toString(), accountId), type, accountId, training);
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragment_cv, fragment).commit();
            fTrans.addToBackStack(null);
        }
    }
}
