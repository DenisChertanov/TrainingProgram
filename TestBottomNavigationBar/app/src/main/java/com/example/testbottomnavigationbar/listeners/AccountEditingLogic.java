package com.example.testbottomnavigationbar.listeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.fragments.AccountEditingFragment;
import com.example.testbottomnavigationbar.fragments.AddExerciseTagFragment;
import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;

public class AccountEditingLogic implements View.OnClickListener {
    private final Account account;
    private final BodyCondition bodyCondition;

    public AccountEditingLogic(Account account, BodyCondition bodyCondition) {
        this.account = account;
        this.bodyCondition = bodyCondition;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new AccountEditingFragment(account, bodyCondition);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment).commit();
        fTrans.addToBackStack(null);
    }
}
