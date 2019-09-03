package com.belicoffee.Model;

import android.net.Uri;

public class ChatMessage {
    //    private String name;
    private String id;
    private String message;
    private String sender;
    private String receiver;
    private String time;
    private Uri avatar;

    public ChatMessage() {
    }

    public ChatMessage(String message, String sender, String receiver, String time, Uri avatar) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
        this.avatar=avatar;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }
}
