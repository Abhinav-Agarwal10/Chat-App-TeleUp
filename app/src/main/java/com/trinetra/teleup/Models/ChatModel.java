package com.trinetra.teleup.Models;

public class ChatModel {
    String uid;

    Long timestamp;

    public ChatModel(String uid, Long timestamp) {
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public ChatModel(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
