package com.example.testbottomnavigationbar.remote_db.tasks;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.SearchFragment;
import com.example.testbottomnavigationbar.remote_db.Exercise;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

public class PullExerciseTask extends AsyncTask<Void, Void, Integer> {
    private final Context context;
    private final ProgressBar progressBar;

    public PullExerciseTask(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    @Override
    protected Integer doInBackground(Void... ignore) {
        try {
            HttpWork worker = new HttpWork();
            Exercise[] exercises = worker.pullAllExercises();

            SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.dbName, Context.MODE_PRIVATE, null);
            for (Exercise exercise : exercises) {
                db.execSQL(exercise.getSQLiteInsertQuery());

                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, exercise.getSQLiteInsertQuery());
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error pulling exercises: " + e, e);
            }
            return 1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(ProgressBar.GONE);

        Fragment fragment = new SearchFragment(SearchFragment.generateExerciseOverviewList(context), 0, null);
        FragmentTransaction fTrans = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment, "searchFragment").commit();
        fTrans.addToBackStack(null);
    }
}
