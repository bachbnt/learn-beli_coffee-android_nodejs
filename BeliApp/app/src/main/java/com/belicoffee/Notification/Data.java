package com.belicoffee.Notification;

import android.net.Uri;

public class Data {
    private String sender;
    private String receiver;
    private String title;
    private String body;
    private String avatar;

    public Data(String sender, String receiver, String title, String body, String avatar) {
        this.sender=sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
        this.avatar=avatar;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
