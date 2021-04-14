package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class Friendship {
    @SerializedName("accountid")
    private int accountId;
    @SerializedName("friendid")
    private int friendId;

    public Friendship(int accountId, int friendId) {
        this.accountId = accountId;
        this.friendId = friendId;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO Friendship(AccountId, FriendId)\n" +
                "VALUES (" +
                "'" +
                accountId +
                "', " +
                "'" +
                friendId +
                "');";
    }

    public String getJSON() {
        return "{" +
                "\"accountid\" : " + accountId + ", " +
                "\"friendid\" : " + friendId + " " +
                "}";
    }

    public int getAccountId() {
        return accountId;
    }

    public int getFriendId() {
        return friendId;
    }
}
