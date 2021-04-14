package com.example.testbottomnavigationbar.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.SQLiteHelper;

import java.io.IOException;

public class ReconnectFragment extends Fragment {
    public ReconnectFragment() {
        super(R.layout.fragment_reconnect);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_reconnect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tuneReconnectButton();
    }

    private void tuneReconnectButton() {
        ProgressBar progressBar = getView().findViewById(R.id.fragment_reconnect_progressbar);

        TextView reconnectButton = getView().findViewById(R.id.fragment_reconnect_reconnect);
        reconnectButton.setVisibility(View.VISIBLE);
        reconnectButton.setOnClickListener(v -> {
            v.setVisibility(View.GONE);
            try {
                SQLiteHelper.handleDBAbsence(v.getContext(), progressBar);
            } catch (IOException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with load DB:  " + e, e);
                }
            }
        });
    }
}
