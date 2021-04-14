package com.example.testbottomnavigationbar.remote_db;

import com.google.gson.annotations.SerializedName;

public class TrainingType {
    @SerializedName("typeid")
    private final int typeId;
    private final String title;

    public TrainingType(int typeId, String title) {
        this.typeId = typeId;
        this.title = title;
    }

    public String getSQLiteInsertQuery() {
        return "INSERT INTO TrainingType(TypeId, Title)\n" +
                "VALUES (" +
                "'" +
                typeId +
                "', " +
                "'" +
                title +
                "');";
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTitle() {
        return title;
    }
}
