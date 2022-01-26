package com.belicoffee.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.belicoffee.Adapter.ChatAdapter;
import com.belicoffee.Manager.ScreenManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.Model.ChatMessage;
import com.belicoffee.Notification.APINotification;
import com.belicoffee.Notification.ChatNotification;
import com.belicoffee.Notification.Client;
import com.belicoffee.Notification.Data;
import com.belicoffee.Notification.Response;
import com.belicoffee.Notification.Token;
import com.belicoffee.R;
import com.belicoffee.Manager.CallManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements View.OnClickListener, UserManager.ChatListener {
    RecyclerView recyclerView;
    EditText etInput;
    ImageButton ibSend;
    ImageButton ibAudio;
    ImageButton ibVideo;
    ChatAdapter chatAdapter;
    ArrayList<ChatMessage> mAllChatMessages;
    ArrayList<ChatMessage> mChatMessages;
    ImageButton ibBack;
    TextView tvTitle;
    FirebaseUser mFirebaseUser;
    String hisId;
    String hisName;
    String myName;
    String myId;
    ScreenManager screenManager;
    UserManager userManager;
    CallManager callManager;
    APINotification apiNotification;
    int j = 1;
    private boolean loading = true;
    int lastFirstVisibleItem, visibleItemCount, totalItemCount;


    public MessageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(String hisId) {
        this.hisId = hisId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ibBack = view.findViewById(R.id.ib_back_message);
        recyclerView = view.findViewById(R.id.rv_message_message);
        etInput = view.findViewById(R.id.et_input_message);
        ibSend = view.findViewById(R.id.ib_send_message);
        tvTitle = view.findViewById(R.id.tv_title_message);
        ibAudio = view.findViewById(R.id.ib_audio_message);
        ibVideo = view.findViewById(R.id.ib_video_message);
        screenManager = ScreenManager.getInstance();
        userManager = UserManager.getInstance();
        callManager = CallManager.getInstance();
        etInput.requestFocus();
        mAllChatMessages = new ArrayList<>();
        mChatMessages = new ArrayList<>();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = mFirebaseUser.getUid();
        setName();


        chatAdapter = new ChatAdapter(getActivity(), mChatMessages, myName, hisName);
        recyclerView.setAdapter(chatAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) //check for scroll up
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (lastFirstVisibleItem == mChatMessages.size() - 10 * j && visibleItemCount < totalItemCount) {
                            j++;
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreMessage(j);
                                    chatAdapter.updateData(mChatMessages, myName, hisName);
                                    chatAdapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(mChatMessages.size() - 10 * (j - 1));
                                }
                            };
                            Handler handler = new Handler();
                            handler.postDelayed(runnable, 500);
                        }
                    }

                }

            }
        });

        apiNotification = Client.getClient("https://fcm.googleapis.com/").create(APINotification.class);
        updateToken(FirebaseInstanceId.getInstance().getToken());
        ibBack.setOnClickListener(this);
        ibSend.setOnClickListener(this);
        ibAudio.setOnClickListener(this);
        ibVideo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMessageList(myId, hisId);
        userManager.setChatListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        userManager.setChatListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back_message:
                screenManager.openScreen(5);
                break;
            case R.id.ib_send_message:
                String message = etInput.getText().toString().trim();
                String time = getCurrentTime();
                if (!message.equals("")) {
                    sendMessage(myId, hisId, message, time);
                    sendNotification(myId, hisId, message, userManager.getMyAvatar(myId).toString());
                    etInput.setText("");
                }
                break;
            case R.id.ib_audio_message:
                callManager.createOffer(myName, hisId,0);
                screenManager.openScreen(9, hisId, hisName,-1);
                break;
            case R.id.ib_video_message:
                callManager.createOffer(myName, hisId,1);
                screenManager.openScreen(9, hisId, hisName,-1);
                break;
        }
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token updatedToken = new Token(token);
        reference.child(mFirebaseUser.getUid()).setValue(updatedToken);
    }

    private void sendMessage(final String sender, final String receiver, final String message, String time) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String chatId = userManager.getChatRoomId(myId, hisId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("time", time);
        reference.child("Chats/" + chatId).push().setValue(hashMap);
    }


    private void sendNotification(final String sender, final String receiver, final String message, final String avatar) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = reference.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(sender, receiver, "Tin nhắn mới từ " + myName, message, avatar);
                    ChatNotification chatNotification = new ChatNotification(data, token.getToken());
                    apiNotification.sendNotification(chatNotification)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadMessageList(final String myId, final String hisId) {
        mAllChatMessages.clear();
        String chatId = userManager.getChatRoomId(myId, hisId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats/" + chatId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAllChatMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    if (chatMessage.getSender().equals(myId) && chatMessage.getReceiver().equals(hisId)) {
                        chatMessage.setAvatar(userManager.getMyAvatar(myId));
                    }
                    if (chatMessage.getSender().equals(hisId) && chatMessage.getReceiver().equals(myId)) {
                        chatMessage.setAvatar(userManager.getHisAvatar(hisId));
                    }
                    mAllChatMessages.add(chatMessage);
                }
                setName();
                loadMoreMessage(j);
                chatAdapter.updateData(mChatMessages, myName, hisName);
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mChatMessages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void loadAvatarMessage() {
        for (ChatMessage chatMessage : mChatMessages) {
            if (chatMessage.getSender().equals(myId) && chatMessage.getReceiver().equals(hisId)) {
                chatMessage.setAvatar(userManager.getMyAvatar(myId));
            }
            if (chatMessage.getSender().equals(hisId) && chatMessage.getReceiver().equals(myId)) {
                chatMessage.setAvatar(userManager.getHisAvatar(hisId));
            }
        }
        chatAdapter.updateData(mChatMessages, myName, hisName);
        chatAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mChatMessages.size() - 1);
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String time, hour, minute;
        if (calendar.get(Calendar.MINUTE) < 10) {
            minute = "0" + calendar.get(Calendar.MINUTE);

        } else {
            minute = "" + calendar.get(Calendar.MINUTE);
        }

        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            hour = "0" + calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            hour = "" + calendar.get(Calendar.HOUR_OF_DAY);
        }

        time = hour + ":" + minute;
        return time;
    }

    public void setName() {
        myName = userManager.getMyName(myId);
        hisName = userManager.getHisName(hisId);
        tvTitle.setText(hisName);
    }

    public void loadMoreMessage(int j) {
        if (mChatMessages.size() < mAllChatMessages.size() - 10) {
            mChatMessages.clear();
            recyclerView.removeAllViewsInLayout();
            for (int i = mAllChatMessages.size() - 10 * j; i < mAllChatMessages.size(); i++) {
                mChatMessages.add(mAllChatMessages.get(i));
            }
        } else {
            mChatMessages.clear();
            recyclerView.removeAllViewsInLayout();
            mChatMessages = (ArrayList<ChatMessage>) mAllChatMessages.clone();
            loading = false;
        }
    }

    @Override
    public void updateAvatar() {
        recyclerView.stopScroll();
        loadAvatarMessage();
    }
}
