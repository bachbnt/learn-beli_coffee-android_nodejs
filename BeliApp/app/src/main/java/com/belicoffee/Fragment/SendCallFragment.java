package com.belicoffee.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.belicoffee.Manager.UserManager;
import com.belicoffee.R;
import com.belicoffee.Manager.CallManager;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SendCallFragment extends Fragment implements View.OnClickListener {
    TextView tvName;
    ImageView ivAvatar;
    ImageButton ibCallOff;
    String hisName;
    String hisId;
    UserManager userManager;
    CallManager callManager;

    @SuppressLint("ValidFragment")
    public SendCallFragment(String hisName, String hisId) {
        this.hisName = hisName;
        this.hisId = hisId;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_call, container, false);
        tvName = view.findViewById(R.id.tv_name_send_call);
        ibCallOff = view.findViewById(R.id.ib_decline_receive_call);
        ivAvatar = view.findViewById(R.id.iv_avatar_receive_call);
        userManager = UserManager.getInstance();
        callManager = CallManager.getInstance();
        tvName.setText(hisName);
        Glide.with(getContext()).load(userManager.getHisAvatar(hisId)).centerCrop().into(ivAvatar);
        ibCallOff.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_decline_receive_call:
                callManager.emitFinish(hisId);
                break;
        }
    }
}
