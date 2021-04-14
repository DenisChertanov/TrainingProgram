package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.listeners.AccountEditingLogic;
import com.example.testbottomnavigationbar.listeners.AddFriendLogic;
import com.example.testbottomnavigationbar.listeners.RemoveFriendLogic;
import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AccountFragment extends Fragment {
    private final int type;
    private final int accountId;
    private Account account;
    private BodyCondition bodyCondition;

    public AccountFragment(int type, int accountId) {
        super(R.layout.fragment_account);
        this.type = type;
        this.accountId = accountId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        account = SQLiteHelper.getAccountFromId(db, accountId);
        bodyCondition = SQLiteHelper.getBodyConditionFromAccountId(db, accountId);

        tuneToolbar(view);
        tuneEditButton();
        tuneLogOutButton();
        tuneTrainingsButton();
        tuneFriendsButton();
        tuneAddFriend();
        fillInfo();
    }

    private void tuneAddFriend() {
        SQLiteDatabase db = getView().getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = getView().getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
        ImageView imageView = getView().findViewById(R.id.fragment_account_add_friend);
        ImageView imageView1 = getView().findViewById(R.id.fragment_account_remove_friend);

        if (type == 0) {
            imageView.setVisibility(View.GONE);
            imageView1.setVisibility(View.GONE);
        } else if (!SQLiteHelper.isFriend(db, currentAccountDB, accountId)) {
            imageView1.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new AddFriendLogic(SQLiteHelper.getCurrentAccountId(currentAccountDB), accountId));
        } else {
            imageView.setVisibility(View.GONE);
            imageView1.setVisibility(View.VISIBLE);
            imageView1.setOnClickListener(new RemoveFriendLogic(SQLiteHelper.getCurrentAccountId(currentAccountDB), accountId));
        }
    }

    private void tuneTrainingsButton() {
        ConstraintLayout constraintLayout = getView().findViewById(R.id.fragment_account_training_constraintlayout);

        if (type == 0) {
            constraintLayout.setVisibility(View.GONE);

            TextView textView = getView().findViewById(R.id.fragment_account_trainings_bottom_stroke);
            textView.setVisibility(View.GONE);
        } else {
            constraintLayout.setOnClickListener(v -> {
                Fragment fragment = new TrainingsFragment(2, accountId);
                FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment_cv, fragment, null).commit();
                fTrans.addToBackStack(null);
            });

            TextView friendsCount = getView().findViewById(R.id.fragment_account_trainings_count);
            int trainingsCnt = getTrainingCnt();
            friendsCount.setText(Integer.valueOf(trainingsCnt).toString());
        }
    }

    private void tuneFriendsButton() {
        TextView friendsCount = getView().findViewById(R.id.fragment_account_friends_count);
        int friendsCnt = getFriendCnt();
        friendsCount.setText(Integer.valueOf(friendsCnt).toString());

        ConstraintLayout constraintLayout = getView().findViewById(R.id.fragment_account_friends_cl);
        constraintLayout.setOnClickListener(v -> {
            Fragment fragment = new FriendsListFragment(type, accountId, FriendsListFragment.generateFriendsListFromAccountId(v.getContext(), accountId));
            FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.fragment_cv, fragment, null).commit();
            fTrans.addToBackStack(null);
        });
    }

    private int getFriendCnt() {
        SQLiteDatabase db = getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        return SQLiteHelper.getFriendsCnt(db, accountId);
    }

    private int getTrainingCnt() {
        SQLiteDatabase db = getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
        return SQLiteHelper.getTrainingCnt(db, accountId);
    }

    private void tuneEditButton() {
        TextView editButton = getView().findViewById(R.id.fragment_account_edit);

        if (type != 0) {
            editButton.setVisibility(View.GONE);
        } else {
            editButton.setOnClickListener(new AccountEditingLogic(account, bodyCondition));
        }
    }

    private void fillInfo() {
        fillName();
        fillUsername();
        fillAge();
        fillHeight();
        fillWidth();
        fillBodyFatShare();
    }

    private void fillName() {
        TextView name = getView().findViewById(R.id.fragment_account_name);
        String strName = account.getFirstName() + " " + account.getSecondName();
        name.setText(strName);
    }

    private void fillUsername() {
        TextView username = getView().findViewById(R.id.fragment_account_username);
        String strUsername = "@" + account.getUserName();
        username.setText(strUsername);
    }

    private void fillAge() {
        TextView age = getView().findViewById(R.id.fragment_account_age);
        String strAge = "Возраст: " + Integer.valueOf(bodyCondition.getAge()).toString();
        age.setText(strAge);
    }

    private void fillHeight() {
        TextView height = getView().findViewById(R.id.fragment_account_height);
        DecimalFormat decimalFormat = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US));
        String strHeight = "Рост: " + decimalFormat.format(bodyCondition.getHeight()) + " см";
        height.setText(strHeight);
    }

    private void fillWidth() {
        TextView width = getView().findViewById(R.id.fragment_account_weight);
        DecimalFormat decimalFormat = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US));
        String strWidth = "Вес: " + decimalFormat.format(bodyCondition.getWeight()) + " кг";
        width.setText(strWidth);
    }

    private void fillBodyFatShare() {
        TextView bodyFatShare = getView().findViewById(R.id.fragmnet_account_percent);
        DecimalFormat decimalFormat = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US));
        String strBodyFatShare = "Процент жира: " + decimalFormat.format(bodyCondition.getBodyFatShare()) + " %";
        bodyFatShare.setText(strBodyFatShare);
    }

    private void tuneLogOutButton() {
        ImageView logOutButton = getView().findViewById(R.id.fragment_account_logout_iv);

        if (type == 0) {
            logOutButton.setOnClickListener(v -> {
//                SQLiteDatabase currentAccountDb = getContext().openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
//                currentAccountDb.execSQL("DELETE FROM CurrentAccount");
//
//                Fragment fragment = new LoginFragment();
//                FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
//                ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                fTrans.replace(R.id.activity_main_fragment_cv, fragment, "loginFragment").commit();
//                fTrans.addToBackStack(null);

                Fragment fragment = new LogOutFragment(accountId);
                FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.activity_main_fragment_cv, fragment, "loginFragment").commit();
                fTrans.addToBackStack(null);
            });
        } else {
            logOutButton.setVisibility(View.GONE);
        }
    }

    private void tuneToolbar(View view) {
        if (type == 0) {
            return;
        }

        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_account_toolbar);
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
