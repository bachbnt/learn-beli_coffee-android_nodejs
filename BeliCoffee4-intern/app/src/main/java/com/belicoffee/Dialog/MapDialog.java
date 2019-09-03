package com.belicoffee.Dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.belicoffee.Manager.MapManager;
import com.belicoffee.Manager.ScreenManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.R;
import com.belicoffee.Manager.CallManager;

@SuppressLint("ValidFragment")
public class MapDialog extends DialogFragment implements View.OnClickListener {
    LinearLayout llChat;
    LinearLayout llAudio;
    LinearLayout llVideo;
    LinearLayout llHistory;
    LinearLayout llDays;
    TextView tv3days, tv7days, tv30days;
    TextView tvName;
    MapManager mapManager;
    ScreenManager screenManager;
    UserManager userManager;
    CallManager callManager;
    String myName;
    String myId;
    String hisId;
    boolean daysState = false;

    HistoryListener historyListener;

    @SuppressLint("ValidFragment")
    public MapDialog(String myName, String myId, String hisId, HistoryListener historyListener) {
        this.myName = myName;
        this.myId = myId;
        this.hisId = hisId;
        this.historyListener = historyListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_map, container, false);
        llChat = view.findViewById(R.id.ll_chat_dialog_map);
        llAudio = view.findViewById(R.id.ll_audio_dialog_map);
        llVideo = view.findViewById(R.id.ll_video_dialog_map);
        llHistory = view.findViewById(R.id.ll_history_dialog_map);
        llDays = view.findViewById(R.id.ll_days_dialog_map);
        tv3days = view.findViewById(R.id.tv_3days_dialog_map);
        tv7days = view.findViewById(R.id.tv_7days_dialog_map);
        tv30days = view.findViewById(R.id.tv_30days_dialog_map);
        tvName = view.findViewById(R.id.tv_name_dialog_map);
        llDays.setVisibility(View.GONE);

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        userManager = UserManager.getInstance();
        screenManager = ScreenManager.getInstance();
        callManager = CallManager.getInstance();
        mapManager = MapManager.getInstance();

        if (hisId == null) {
            llChat.setVisibility(View.GONE);
            llAudio.setVisibility(View.GONE);
            llVideo.setVisibility(View.GONE);
            tvName.setText("Tôi là " + myName);
        } else {
            tvName.setText(userManager.getHisName(hisId));
        }

        llChat.setOnClickListener(this);
        llAudio.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llHistory.setOnClickListener(this);
        tv3days.setOnClickListener(this);
        tv7days.setOnClickListener(this);
        tv30days.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_chat_dialog_map:
                screenManager.openScreen(6, hisId, null,-1);
                getDialog().dismiss();
                break;
            case R.id.ll_audio_dialog_map:
                callManager.createOffer(myName, hisId,0);
                screenManager.openScreen(9, hisId, userManager.getHisName(hisId),-1);
                getDialog().dismiss();
                break;
            case R.id.ll_video_dialog_map:
                callManager.createOffer(myName, hisId,1);
                screenManager.openScreen(9, hisId, userManager.getHisName(hisId),-1);
                getDialog().dismiss();
                break;
            case R.id.ll_history_dialog_map:
                if (!daysState) {
                    llHistory.setBackgroundResource(R.color.colorWhite);
                    llDays.setVisibility(View.VISIBLE);
                } else {
                    llHistory.setBackgroundResource(R.drawable.bg_dialog_user);
                    llDays.setVisibility(View.GONE);
                }
                daysState = !daysState;
                break;
            case R.id.tv_3days_dialog_map:
                mapManager.setHistory(true);
                if (hisId == null)
                    mapManager.loadAllHistory(myId, 3);
                else mapManager.loadAllHistory(hisId, 3);
                getDialog().dismiss();
                historyListener.showButton();
                break;
            case R.id.tv_7days_dialog_map:
                mapManager.setHistory(true);
                if (hisId == null)
                    mapManager.loadAllHistory(myId, 7);
                else mapManager.loadAllHistory(hisId, 7);
                getDialog().dismiss();
                historyListener.showButton();
                break;
            case R.id.tv_30days_dialog_map:
                mapManager.setHistory(true);
                Log.i("tag", "historystate");

                if (hisId == null)
                    mapManager.loadAllHistory(myId, 30);
                else mapManager.loadAllHistory(hisId, 30);
                getDialog().dismiss();
                historyListener.showButton();
                break;
        }
    }

    public interface HistoryListener {
        void showButton();
    }
}
