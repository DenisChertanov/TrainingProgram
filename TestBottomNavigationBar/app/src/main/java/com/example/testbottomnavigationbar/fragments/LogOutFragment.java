package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.listeners.RemoveAccountLogic;

public class LogOutFragment extends Fragment {
    private final int accountId;

    public LogOutFragment(int accountId) {
        super(R.layout.fragment_log_out);
        this.accountId = accountId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneLogOutButton();
        tuneDeleteButton();
        tuneToolbar(view);
    }

    private void tuneLogOutButton() {
        TextView logOutButton = getView().findViewById(R.id.fragment_log_out_log_out);
        logOutButton.setOnClickListener(v -> {
            SQLiteDatabase currentAccountDb = getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
            currentAccountDb.execSQL("DELETE FROM CurrentAccount");

            Fragment fragment = new LoginFragment();
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fTrans.replace(R.id.activity_main_fragment_cv, fragment, "loginFragment").commit();
            fTrans.addToBackStack(null);
        });
    }

    private void tuneDeleteButton() {
        TextView deleteButton = getView().findViewById(R.id.fragment_log_out_delete);
        deleteButton.setOnClickListener(new RemoveAccountLogic(accountId));
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_log_out_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
