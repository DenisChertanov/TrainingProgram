package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.AddExerciseDescriptionFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashSet;

public class NewExerciseFilterLogic implements View.OnClickListener {
    private HashSet<String> tags;
    private HashSet<String> filters;
    private String title;

    public NewExerciseFilterLogic(HashSet<String> tags) {
        filters = new HashSet<>();
        this.tags = tags;
    }

    public void addFilter(String filter) {
        filters.add(filter);
    }

    public void removeFilter(String filter) {
        filters.remove(filter);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onClick(View v) {
        if (!findTitle(v)) {
            return;
        }

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "title   " + title);
        }

        Fragment fragment = new AddExerciseDescriptionFragment(tags, filters, title);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }

    private boolean findTitle(View view) {
        EditText editText = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_add_exercise_title_et);
        title = editText.getText().toString();

        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        int titleId = SQLiteHelper.getExerciseIdFromTitle(db, title);

        return (!title.equals("") && titleId == -1);
    }
}
