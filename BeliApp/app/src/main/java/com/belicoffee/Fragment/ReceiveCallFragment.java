package com.belicoffee.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.belicoffee.Manager.ScreenManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.R;
import com.belicoffee.Manager.CallManager;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ReceiveCallFragment extends Fragment implements View.OnClickListener {
    TextView tvName;
    ImageButton ibAccept;
    ImageButton ibDecline;
    String hisName;
    UserManager userManager;
    ScreenManager screenManager;
    CallManager callManager;
    String hisId;
    Vibrator vibrator;
    ImageView ivAvatar;

    @SuppressLint("ValidFragment")
    public ReceiveCallFragment(String hisName, String hisId) {
        this.hisName = hisName;
        this.hisId = hisId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receive_call, container, false);
        screenManager = ScreenManager.getInstance();
        userManager = UserManager.getInstance();
        callManager = CallManager.getInstance();
        tvName = view.findViewById(R.id.tv_name_send_call);
        ibAccept = view.findViewById(R.id.ib_accept_receive_call);
        ibDecline = view.findViewById(R.id.ib_decline_receive_call);
        ivAvatar = view.findViewById(R.id.iv_avatar_receive_call);
        tvName.setText(hisName);
        Glide.with(getContext()).load(userManager.getHisAvatar(hisId)).centerCrop().into(ivAvatar);

//        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {1000, 500, 1000, 500};
//        vibrator.vibrate(pattern, 0);
        ibAccept.setOnClickListener(this);
        ibDecline.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
//        vibrator.cancel();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_accept_receive_call:
                callManager.createAnswer(true, hisId);
                break;
            case R.id.ib_decline_receive_call:
                callManager.createAnswer(false, hisId);
        }
    }
}
