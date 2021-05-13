package com.example.quang.chatbot.Model;

import org.json.JSONArray;

public class Message {
    private String text;
    private String sender;
    private boolean isMine;
    private String type;
    private JSONArray table;

    public String getType() {
        return type;
    }

    public JSONArray getTable() {
        return table;
    }

    public void setTable(JSONArray table) {
        this.table = table;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Message(String text,boolean isMine,String type){
        this.text=text;
        this.sender="sender";
        this.isMine=isMine;
        this.type=type;
    }

}