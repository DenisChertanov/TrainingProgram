package com.example.testbottomnavigationbar.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.remote_db.Training;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {
    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneBottomNavigationBar();
        setStartFragment(getContext());
    }

    @SuppressLint("NonConstantResourceId")
    private void tuneBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = getView().findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_bottom_nav__search:
                    if (isFragmentVisible("searchFragment")) {
                        break;
                    }
                    setCurrentFragment(new SearchFragment(SearchFragment.generateExerciseOverviewList(getContext()), 0, null), "searchFragment");
                    break;
                case R.id.menu_bottom_nav__trainings:
                    if (isFragmentVisible("trainingsFragment")) {
                        break;
                    }
                    SQLiteDatabase currentAccountDB0 = getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
                    int accountId0 = SQLiteHelper.getCurrentAccountId(currentAccountDB0);
                    setCurrentFragment(new TrainingsFragment(1, accountId0), "trainingsFragment");
                    break;
                case R.id.menu_bottom_nav__timetable:
                    if (isFragmentVisible("timetableFragment")) {
                        break;
                    }
                    SQLiteDatabase db = getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
                    SQLiteDatabase currentAccountDB = getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

                    int trainingId = SQLiteHelper.getCurrentTrainingId(db, currentAccountDB);
                    String trainingTitle = SQLiteHelper.getTrainingTitleFromId(db, trainingId);
                    int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
                    setCurrentFragment(new TimeTableFragment(SQLiteHelper.getCurrentTrainingId(db, currentAccountDB), 0, accountId, new Training(trainingId, trainingTitle, accountId)), "timetableFragment");
                    break;
                case R.id.menu_bottom_nav__account:
                    if (isFragmentVisible("accountFragment")) {
                        break;
                    }
                    SQLiteDatabase currentAccountDB1 = getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
                    setCurrentFragment(new AccountFragment(0, SQLiteHelper.getCurrentAccountId(currentAccountDB1)), "accountFragment");
                    break;
            }
            return true;
        });
    }

    private void setStartFragment(Context context) {
        Fragment fragment = new SearchFragment(SearchFragment.generateExerciseOverviewList(context), 0, null);
        FragmentTransaction fTrans = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment, "searchFragment").commit();
        fTrans.addToBackStack(null);
    }

    private void setCurrentFragment(Fragment fragment, String fragmentName) {
        clearBackStack();
        FragmentTransaction fTrans = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment, fragmentName).commit();
        fTrans.addToBackStack(null);
    }

    private boolean isFragmentVisible(String fragmentName) {
        Fragment fragment = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag(fragmentName);
        return (fragment != null && fragment.isVisible());
    }

    private void clearBackStack() {
        ((FragmentActivity) getContext()).getSupportFragmentManager().popBackStackImmediate("mainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
