package com.example.testbottomnavigationbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testbottomnavigationbar.fragments.AccountFragment;
import com.example.testbottomnavigationbar.fragments.SearchFragment;
import com.example.testbottomnavigationbar.fragments.TimeTableFragment;
import com.example.testbottomnavigationbar.fragments.TrainingsFragment;
import com.example.testbottomnavigationbar.remote_db.Training;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final boolean LOG = true;
    public static final String TEG = "dchertanov_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteHelper.handleDBAbsence(this, (ProgressBar) findViewById(R.id.activity_main_progressbar));
        } catch (IOException e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Problem with load DB:  " + e, e);
            }
        }
    }

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}