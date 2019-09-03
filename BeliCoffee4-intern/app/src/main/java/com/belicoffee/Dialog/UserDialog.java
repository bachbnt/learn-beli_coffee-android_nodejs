package com.belicoffee.Dialog;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.belicoffee.Manager.ScreenManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.Model.ChatUser;
import com.belicoffee.R;
import com.belicoffee.Manager.CallManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class UserDialog extends DialogFragment implements View.OnClickListener {
    LinearLayout llAudio;
    LinearLayout llVideo;
    ChatUser mChatUser;
    String myId;
    String myName;
    ScreenManager screenManager;
    UserManager userManager;
    CallManager callManager;


    @SuppressLint("ValidFragment")
    public UserDialog(String myId, String myName, ChatUser chatUser) {
        this.mChatUser = chatUser;
        this.myId = myId;
        this.myName = myName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_user, container, false);
        llAudio = view.findViewById(R.id.ll_audio_dialog_user);
        llVideo = view.findViewById(R.id.ll_video_dialog_user);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        screenManager = ScreenManager.getInstance();
        callManager = CallManager.getInstance();
        userManager = UserManager.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();
        llAudio.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_audio_dialog_user:
                callManager.createOffer(myName, mChatUser.getId(),0);
                screenManager.openScreen(9, mChatUser.getId(), userManager.getHisName(mChatUser.getId()),-1);
                getDialog().dismiss();
                break;
            case R.id.ll_video_dialog_user:
                callManager.createOffer(myName, mChatUser.getId(),1);
                screenManager.openScreen(9, mChatUser.getId(), userManager.getHisName(mChatUser.getId()),-1);
                getDialog().dismiss();
                break;
        }
    }
}
