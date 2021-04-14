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
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.testbottomnavigationbar.adapters.TrainingAdapter;
import com.example.testbottomnavigationbar.listeners.AddTrainingLogic;
import com.example.testbottomnavigationbar.remote_db.Training;

import java.util.ArrayList;
import java.util.List;

public class TrainingsFragment extends Fragment {
    private final int type;
    private final int accountId;

    public TrainingsFragment(int type, int accountId) {
        super(R.layout.fragment_trainings);
        this.type = type;
        this.accountId = accountId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_trainings, container, false);
    }

    private void fillRecyclerView(List<Training> trainings) {
        RecyclerView recyclerView = getView().findViewById(R.id.fragment_trainings_recyclerview);
        TrainingAdapter trainingAdapter = new TrainingAdapter(type, accountId, trainings);
        recyclerView.setAdapter(trainingAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (type != 1) {
            handleType();
        }

        fillRecyclerView(generateTrainingsList(view.getContext(), accountId));
        tuneEditText(view.getContext());
        tuneAddTraining();
    }

    private void handleType() {
        TextView textView = getView().findViewById(R.id.fragment_trainings_header);
        textView.setVisibility(View.GONE);

        textView = getView().findViewById(R.id.fragment_trainins_title_line);
        textView.setVisibility(View.GONE);

        Toolbar toolbar = ((FragmentActivity) getView().getContext()).findViewById(R.id.fragment_trainings_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        ((AppCompatActivity) getView().getContext()).setSupportActionBar(toolbar);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "toolbar " + (toolbar == null));
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v1 -> ((FragmentActivity) v1.getContext()).getSupportFragmentManager().popBackStack());
        ((AppCompatActivity) getView().getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void tuneAddTraining() {
        ImageView imageView = getView().findViewById(R.id.fragment_trainings_add_button);

        if (type == 1) {
            imageView.setOnClickListener(new AddTrainingLogic());
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    private void tuneEditText(Context context) {
        EditText editText = getView().findViewById(R.id.fragment_trainings_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fillRecyclerView(generateTrainingsListFromTitle(context, s.toString(), accountId));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if(v.getId() == R.id.fragment_trainings_edittext && !hasFocus) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private List<Training> generateTrainingsListFromTitle(Context context, String prefix, int accountId) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
//        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        ArrayList<Training> list = new ArrayList<>();

//        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        Cursor cursor = db.rawQuery("SELECT Title FROM Training WHERE Title LIKE '" + prefix + "%' AND AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                list.add(new Training(SQLiteHelper.getTrainingIdFromTitle(db, title, accountId), title, accountId));
            } while(cursor.moveToNext());
        }

        return list;
    }

    private List<Training> generateTrainingsList(Context context, int accountId) {
        SQLiteDatabase db = context.openOrCreateDatabase(SQLiteHelper.getDbName(), Context.MODE_PRIVATE, null);
//        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(SQLiteHelper.getCurrentAccountDBName(), Context.MODE_PRIVATE, null);

        ArrayList<Training> list = new ArrayList<>();

//        int accountId = SQLiteHelper.getCurrentAccountId(currentAccountDB);
        Cursor cursor = db.rawQuery("SELECT Title FROM Training WHERE AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                list.add(new Training(SQLiteHelper.getTrainingIdFromTitle(db, title, accountId), title, accountId));
            } while(cursor.moveToNext());
        }

        return list;
    }
}
