package com.example.testbottomnavigationbar.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;
import com.example.testbottomnavigationbar.adapters.ExerciseOverviewAdapter;
import com.example.testbottomnavigationbar.adapters.FriendOverviewAdapter;
import com.example.testbottomnavigationbar.remote_db.Account;

import java.util.ArrayList;
import java.util.List;

public class FriendsListFragment extends Fragment {
    private final int type;
    private final int accountId;
    private List<Account> friendsList;

    public FriendsListFragment(int type, int accountId, List<Account> friendsList) {
        super(R.layout.fragment_friends_search);
        this.type = type;
        this.accountId = accountId;
        this.friendsList = friendsList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_friends_search, container, false);
    }

    private void fillRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.fragment_friends_search_recyclerview);
        FriendOverviewAdapter friendOverviewAdapter = new FriendOverviewAdapter(type, accountId, friendsList);
        recyclerView.setAdapter(friendOverviewAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneToolbar(view);
        tuneEditText(view.getContext());

        fillRecyclerView();
    }

    private void tuneEditText(Context context) {
        EditText editText = getView().findViewById(R.id.fragment_friends_search_et);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                MainActivity.hideSoftKeyboard(v.getContext(), v);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "EditText " + s);
                }

                friendsList = generateFriendsListFromPrefix(context, s.toString(), accountId);
                fillRecyclerView();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static List<Account> generateFriendsListFromAccountId(Context context, int accountId) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
        int currentAccountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        List<Account> friendsList = new ArrayList<>();

        Cursor friendsCursor = db.rawQuery("SELECT FriendId FROM Friendship WHERE AccountId = " + accountId + " AND FriendId <> " + currentAccountId + ";", null);
        if (friendsCursor != null && friendsCursor.moveToFirst()) {
            do {
                int friendId = friendsCursor.getInt(0);

                Cursor friendAccountCursor = db.rawQuery("SELECT * FROM Account WHERE AccountId = " + friendId + ";", null);
                if (friendAccountCursor != null && friendAccountCursor.moveToFirst()) {
                    friendsList.add(new Account(
                            friendId,
                            friendAccountCursor.getString(1),
                            friendAccountCursor.getString(2),
                            friendAccountCursor.getString(3),
                            friendAccountCursor.getString(4),
                            friendAccountCursor.getString(5),
                            (friendAccountCursor.isNull(6) ? null : friendAccountCursor.getInt(6)),
                            friendAccountCursor.getString(7),
                            friendAccountCursor.getString(8)
                    ));
                }
            } while (friendsCursor.moveToNext());
        }

        return friendsList;
    }

    public static List<Account> generateFriendsListFromPrefix(Context context, String prefix, int accountId) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);

        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);
        int currentAccountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);

        List<Account> friendsList = new ArrayList<>();

//        Cursor friendsCursor = db.rawQuery("SELECT FriendId FROM Friendship WHERE AccountId = " + accountId + " AND Username LIKE '" + prefix + "%';", null);
        Cursor friendAccountCursor = db.rawQuery("SELECT * FROM Account WHERE AccountId <> " + accountId + " AND Username LIKE '" + prefix + "%' AND AccountId <> " + currentAccountId +";", null);
        if (friendAccountCursor != null && friendAccountCursor.moveToFirst()) {
            do {
                friendsList.add(new Account(
                        friendAccountCursor.getInt(0),
                        friendAccountCursor.getString(1),
                        friendAccountCursor.getString(2),
                        friendAccountCursor.getString(3),
                        friendAccountCursor.getString(4),
                        friendAccountCursor.getString(5),
                        (friendAccountCursor.isNull(6) ? null : friendAccountCursor.getInt(6)),
                        friendAccountCursor.getString(7),
                        friendAccountCursor.getString(8)
                ));
            } while (friendAccountCursor.moveToNext());
        }

        return friendsList;
    }

    private void tuneToolbar(View view) {
        Toolbar toolbar = ((FragmentActivity) view.getContext()).findViewById(R.id.fragment_friends_search_toolbar);
        ((AppCompatActivity) view.getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) view.getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
