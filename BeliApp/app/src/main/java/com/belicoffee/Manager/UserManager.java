package com.belicoffee.Manager;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.belicoffee.Model.ChatMessage;
import com.belicoffee.Model.ChatUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserManager {
    static UserManager userManager;
    ArrayList<ChatUser> chatUsers;
    ChatUser self;
    UserListener userListener;
    ChatListener chatListener;

    private UserManager() {
        chatUsers = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (userManager == null)
            userManager = new UserManager();
        return userManager;
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }

    public ArrayList<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void clearUsers() {
        chatUsers.clear();
    }

    public String getMyName(String myId) {
        String myName = null;
        if (self != null && self.getId().equals(myId))
            myName = self.getUsername();
        return myName;
    }

    public String getHisName(String hisId) {
        String hisName = null;
        for (ChatUser chatUser : chatUsers) {
            if (chatUser != null && chatUser.getId().equals(hisId))
                hisName = chatUser.getUsername();
        }
        return hisName;
    }

    public Uri getMyAvatar(String myId) {
        Uri myAvatar = null;
        if (self != null && self.getId().equals(myId))
            myAvatar = self.getAvatar();
        return myAvatar;
    }

    public Uri getHisAvatar(String hisId) {
        Uri hisAvatar = null;
        for (ChatUser chatUser : chatUsers) {
            if (chatUser != null && chatUser.getId().equals(hisId))
                hisAvatar = chatUser.getAvatar();
        }
        return hisAvatar;
    }

    public void notifyUserChange() {
        if (userListener != null)
            userListener.updateUser();
    }

    public void notifyChatChange() {
        if (chatListener != null)
            chatListener.updateAvatar();
    }


    public String getChatRoomId(String myId, String hisId) {
        String chatId = "";
        if (myId.compareTo(hisId) < 0) {
            chatId = myId + hisId;
        } else chatId = hisId + myId;
        return chatId;
    }

    public void loadUserList(final String myId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatUser chatUser = snapshot.getValue(ChatUser.class);
                    if (!chatUser.getId().equals(myId)) {
                        chatUsers.add(chatUser);
                    } else self = chatUser;
                }
                for (ChatUser chatUser : chatUsers) {
                    loadLastMsg(chatUser, myId);
                    loadAvatar(chatUser);
                }
                loadAvatar(self);
                notifyUserChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadAvatar(final ChatUser chatUser) {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Avatars/" + chatUser.getId());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                chatUser.setAvatar(uri);
                notifyUserChange();
                notifyChatChange();
            }
        });
    }

    public void loadLastMsg(final ChatUser chatUser, final String myId) {
        final ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        String chatId = getChatRoomId(myId, chatUser.getId());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats/" + chatId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage);
                    chatUser.setLastMessage(chatMessages.get(chatMessages.size() - 1).getMessage());
                    chatUser.setLastTime(chatMessages.get(chatMessages.size()-1).getTime());
                }
                notifyUserChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface UserListener {
        void updateUser();
    }

    public interface ChatListener {
        void updateAvatar();
    }
}
