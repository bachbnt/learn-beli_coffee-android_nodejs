package com.belicoffee.Model;

import android.net.Uri;

public class ChatUser {
    private String id;
    private String username;
    private String email;
    private String phone;
    private Uri avatar;
    private String lastMessage;
    private String lastTime;

    public ChatUser() {
    }

    public ChatUser(String id, String username, String email, String phone, Uri avatar, String lastMessage, String lastTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
