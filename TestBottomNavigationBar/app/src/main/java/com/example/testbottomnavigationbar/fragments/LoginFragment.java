package com.example.testbottomnavigationbar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.listeners.SignInLogic;

public class LoginFragment extends Fragment {
    public LoginFragment() {
        super(R.layout.login_layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.login_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneUsername();
        tunePassword();
        tuneSignInButton();
        tuneSignUpButton();
    }

    private void tuneUsername() {
        EditText username = getView().findViewById(R.id.login_layout_login_et);
        username.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void tunePassword() {
        EditText password = getView().findViewById(R.id.login_layout_password_et);
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });
    }

    private void tuneSignInButton() {
        TextView signInButton = getView().findViewById(R.id.login_layout_login);
        signInButton.setOnClickListener(new SignInLogic());
    }

    private void tuneSignUpButton() {
        TextView sigUpButton = getView().findViewById(R.id.login_layout_sign_up);
        sigUpButton.setOnClickListener(v -> {
            Fragment fragment = new SignUpFragment();
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.activity_main_fragment_cv, fragment, "signUpFragment").commit();
            fTrans.addToBackStack(null);
        });
    }
}
