package com.example.testbottomnavigationbar.remote_db.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.remote_db.Friendship;
import com.example.testbottomnavigationbar.remote_db.HttpWork;

import java.io.IOException;
import java.net.MalformedURLException;

public class DeleteFriendshipTask extends AsyncTask<Void, Void, Integer> {
    private final Friendship friendship;

    public DeleteFriendshipTask(Friendship friendship) {
        this.friendship = friendship;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            HttpWork worker = null;
            try {
                worker = new HttpWork();
            } catch (MalformedURLException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with creating httpwork   " + e, e);
                }
            }
            try {
                worker.deleteFriendship(friendship);
            } catch (IOException e) {
                if (MainActivity.LOG) {
                    Log.d(MainActivity.TEG, "Problem with removing Friendship:   " + e, e);
                }
            }

            return 0;
        } catch (Exception e) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "Error remove in remote db: " + e, e);
            }
            return 1;
        }
    }
}
