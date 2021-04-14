package com.example.testbottomnavigationbar.remote_db.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.LoginFragment;
import com.example.testbottomnavigationbar.fragments.MainFragment;
import com.example.testbottomnavigationbar.fragments.ReconnectFragment;
import com.example.testbottomnavigationbar.fragments.SearchFragment;
import com.example.testbottomnavigationbar.remote_db.Exercise;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

public class PullRemoteDBTask extends AsyncTask<Void, Void, Integer> {
    private final Context context;
    private final ProgressBar progressBar;

    public PullRemoteDBTask(Context context, ProgressBar progressBar) {
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
            SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.dbName, Context.MODE_PRIVATE, null);

            SQLiteHelper.pullAccount(worker, db);
            SQLiteHelper.pullExercise(worker, db);
            SQLiteHelper.pullExerciseTag(worker, db);
            SQLiteHelper.pullExerciseToTag(worker, db);
            SQLiteHelper.pullTraining(worker, db);
            SQLiteHelper.pullTrainingExercise(worker, db);
            SQLiteHelper.pullTrainingExerciseNote(worker, db);
            SQLiteHelper.pullTrainingExerciseSuccess(worker, db);
            SQLiteHelper.pullTrainingType(worker, db);
            SQLiteHelper.pullExerciseTrainingType(worker, db);
            SQLiteHelper.pullMuscle(worker, db);
            SQLiteHelper.pullTargetMuscle(worker, db);
            SQLiteHelper.pullBodyCondition(worker, db);
            SQLiteHelper.pullFriendship(worker, db);

//            db.execSQL("INSERT INTO CurrentAccount(AccountId)\n" +
//                    "VALUES ('1')");

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error pulling remote db: " + e, e);
            }
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(ProgressBar.GONE);

        if (integer == 0) {
            if (!hasCurrentAccount(context)) {
                setLoginFragment(context);
            } else {
                setMainFragment(context);
            }
        } else if (integer == -1) {
            Fragment fragment = new ReconnectFragment();
            FragmentTransaction fTrans = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.activity_main_fragment_cv, fragment, "reconnectFragment").commit();
            fTrans.addToBackStack(null);
        }
    }

    private boolean hasCurrentAccount(Context context) {
        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.currentAccountDBName, Context.MODE_PRIVATE, null);
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.dbName, Context.MODE_PRIVATE, null);

        Cursor cursor = currentAccountDB.rawQuery("SELECT * FROM CurrentAccount;", null);
        if (cursor != null && cursor.moveToFirst()) {
            int accountId = cursor.getInt(0);

            Cursor cursor1 = db.rawQuery("SELECT * FROM Account WHERE AccountId = " + accountId + ";", null);
            if (cursor1 != null && cursor1.moveToFirst()) {
                return true;
            }
        }

        return false;
    }

    private void setLoginFragment(Context context) {
        Fragment fragment = new LoginFragment();
        FragmentTransaction fTrans = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.activity_main_fragment_cv, fragment, "loginFragment").commit();
        fTrans.addToBackStack(null);
    }

    private void setMainFragment(Context context) {
        Fragment fragment = new MainFragment();
        FragmentTransaction fTrans = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.activity_main_fragment_cv, fragment, "mainFragment").commit();
        fTrans.addToBackStack(null);
    }
}
