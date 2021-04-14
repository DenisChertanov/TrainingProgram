package com.example.testbottomnavigationbar.listeners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.fragments.AccountFragment;
import com.example.testbottomnavigationbar.remote_db.Friendship;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertFriendshipTask;

public class AddFriendLogic implements View.OnClickListener {
    private final int accountId;
    private final int friendId;

    public AddFriendLogic(int accountId, int friendId) {
        this.accountId = accountId;
        this.friendId = friendId;
    }

    @Override
    public void onClick(View v) {
        addFriendInDb(v);

        ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStack();
        Fragment fragment = new AccountFragment(1, friendId);
        FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_cv, fragment, null).commit();
        fTrans.addToBackStack(null);
    }

    private void addFriendInDb(View view) {
        SQLiteDatabase db = view.getContext().openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        db.execSQL("INSERT INTO Friendship(AccountId, FriendId)\n" +
                "VALUES ('" +  accountId + "', '" + friendId + "');");

        addFriendInRemoteDB();
    }

    private void addFriendInRemoteDB() {
        new InsertFriendshipTask(new Friendship(accountId, friendId)).execute();
    }
}
