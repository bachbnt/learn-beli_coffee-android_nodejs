package com.belicoffee.Fragment;


import android.annotation.SuppressLint;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
public class CallFragment extends Fragment implements View.OnClickListener, CallManager.RemoteListener {
    GLSurfaceView svVideo;
    CallManager callManager;
    ImageButton ibCallOff;
    ImageButton ibMicro;
    ImageButton ibCamera;
    ImageButton ibSpeaker;
    ImageButton ibSwitch;
    String hisId;
    ImageView ivAvatar,ivAudio;
    CardView cvAvatar,cvAudio;
    TextView tvTitle,tvName;
    boolean micro = true;
    boolean camera = true;
    boolean speaker = true;
    UserManager userManager;
    int type;

    @SuppressLint("ValidFragment")
    public CallFragment(String hisId, int type) {
        this.hisId = hisId;
        this.type = type;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        userManager = UserManager.getInstance();
        ibCallOff = view.findViewById(R.id.ib_call_off_call);
        ibMicro = view.findViewById(R.id.ib_micro_call);
        ibCamera = view.findViewById(R.id.ib_camera_call);
        ibSwitch = view.findViewById(R.id.ib_switch_camera_call);
        ibSpeaker = view.findViewById(R.id.ib_speaker_call);
        ivAvatar = view.findViewById(R.id.iv_avatar_call);
        cvAvatar = view.findViewById(R.id.cv_avatar_call);
        ivAudio=view.findViewById(R.id.iv_audio_call);
        cvAudio=view.findViewById(R.id.cv_audio_call);
        tvTitle = view.findViewById(R.id.tv_title_call);
        tvName=view.findViewById(R.id.tv_name_call);
        svVideo = view.findViewById(R.id.glsv_video_call);
        ibCallOff.setOnClickListener(this);
        ibMicro.setOnClickListener(this);
        ibCamera.setOnClickListener(this);
        ibSpeaker.setOnClickListener(this);
        ibSwitch.setOnClickListener(this);

        Glide.with(getContext()).load(userManager.getHisAvatar(hisId)).centerCrop().into(ivAvatar);
        Glide.with(getContext()).load(userManager.getHisAvatar(hisId)).centerCrop().into(ivAudio);

        cvAvatar.setVisibility(View.GONE);
        cvAudio.setVisibility(View.GONE);
        tvName.setText(userManager.getHisName(hisId));
        tvName.setVisibility(View.GONE);
        tvTitle.setText(userManager.getHisName(hisId) + " đã tắt Camera");
        tvTitle.setVisibility(View.GONE);

        callManager = CallManager.getInstance();
        callManager.setRemoteListener(this, getActivity());
        callManager.setVideoRenderer(svVideo);
        callManager.setAudioManager(!speaker);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        callManager.setAudioTrack(true);
        callManager.setVideoTrack(true);
        if (type == 0) {
            svVideo.setVisibility(View.GONE);
            ibSwitch.setVisibility(View.GONE);
            ibCamera.setVisibility(View.GONE);
            cvAudio.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.VISIBLE);
            ibSpeaker.callOnClick();
        }
    }

        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.ib_call_off_call:
                    callManager.emitFinish(hisId);
                    break;
                case R.id.ib_micro_call:
                    if (micro) {
                        ibMicro.setImageResource(R.drawable.ic_mic_off_white_32dp);
                    } else {
                        ibMicro.setImageResource(R.drawable.ic_mic_white_32dp);
                    }
                    callManager.setAudioTrack(!micro);
                    micro = !micro;
                    break;
                case R.id.ib_camera_call:
                    if (camera) {
                        ibCamera.setImageResource(R.drawable.ic_videocam_off_white_32dp);
                    } else {
                        ibCamera.setImageResource(R.drawable.ic_videocam_white_32dp);
                    }
                    callManager.emitType(!camera);
                    callManager.setVideoTrack(!camera);
                    camera = !camera;
                    break;
                case R.id.ib_speaker_call:
                    if (speaker)
                        ibSpeaker.setImageResource(R.drawable.ic_volume_down_white_32dp);
                    else
                        ibSpeaker.setImageResource(R.drawable.ic_volume_up_white_32dp);
                    callManager.setAudioManager(speaker);
                    speaker = !speaker;
                    break;
                case R.id.ib_switch_camera_call:
                    callManager.switchCamera();
                    break;
            }
        }

        @Override
        public void setVisibleRemoteVideo ( boolean type){
            if (!type) {
                cvAvatar.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
            } else {
                cvAvatar.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
            }
        }
    }
