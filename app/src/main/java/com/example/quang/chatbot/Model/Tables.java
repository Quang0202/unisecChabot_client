package com.example.quang.chatbot.Model;

import org.json.JSONArray;

import java.util.List;

public class Tables {
    private JSONArray table;
    private int index;
    public Tables(JSONArray table, int index) {
        this.table = table;
        this.index=index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public JSONArray getTable() {
        return table;
    }

    public void setTable(JSONArray table) {
        this.table = table;
    }
}
