package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.listeners.SaveAccountEditingLogic;
import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AccountEditingFragment extends Fragment {
    private final Account account;
    private final BodyCondition bodyCondition;

    public AccountEditingFragment(Account account, BodyCondition bodyCondition) {
        super(R.layout.account_editing);
        this.account = account;
        this.bodyCondition = bodyCondition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.account_editing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneToolbar(view);
        tuneET();
        tuneSaveButton();
    }

    private void tuneSaveButton() {
        TextView saveButton = getView().findViewById(R.id.account_editing_save);
        saveButton.setOnClickListener(new SaveAccountEditingLogic(account, bodyCondition));
    }

    private void tuneET() {
        tuneFirstName();
        tuneSecondName();
        tuneUsername();
        tuneAge();
        tuneWeight();
        tuneHeight();
        tuneBodyFatShare();
    }

    private void tuneFirstName() {
        EditText firstName = getView().findViewById(R.id.account_editing_name_edit);
        firstName.setText(account.getFirstName());

        setHideKeyBoard(firstName);
    }

    private void tuneSecondName() {
        EditText secondName = getView().findViewById(R.id.account_editing_second_name_edit);
        secondName.setText(account.getSecondName());

        setHideKeyBoard(secondName);
    }

    private void tuneUsername() {
        EditText username = getView().findViewById(R.id.account_editing_second_username_edit);
        username.setText(account.getUserName());

        setHideKeyBoard(username);
    }

    private void tuneAge() {
        EditText age = getView().findViewById(R.id.account_editing_age_edit);
        age.setText(Integer.valueOf(bodyCondition.getAge()).toString());

        setHideKeyBoard(age);
    }

    private void tuneWeight() {
        EditText width = getView().findViewById(R.id.account_editing_weight_edit);
        DecimalFormat decimalFormat = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US));
        width.setText(decimalFormat.format(bodyCondition.getWeight()));

        setHideKeyBoard(width);
    }

    private void tuneHeight() {
        EditText height = getView().findViewById(R.id.account_editing_height_edit);
        DecimalFormat decimalFormat = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US));
        height.setText(decimalFormat.format(bodyCondition.getHeight()));

        setHideKeyBoard(height);
    }

    private void tuneBodyFatShare() {
        EditText bodyFatShare = getView().findViewById(R.id.account_editing_percent_edit);
        DecimalFormat decimalFormat = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US));
        bodyFatShare.setText(decimalFormat.format(bodyCondition.getBodyFatShare()));

        setHideKeyBoard(bodyFatShare);
    }

    private void setHideKeyBoard(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.account_editing_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
