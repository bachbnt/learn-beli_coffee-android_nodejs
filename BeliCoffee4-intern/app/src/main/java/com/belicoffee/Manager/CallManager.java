package com.belicoffee.Manager;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CallManager {
    private static final boolean accept = true;
    private static final boolean decline = false;

    private static final String SIGNALING_URI = "https://belicoffee.herokuapp.com";
    //    private static final String SIGNALING_URI = "http://172.16.1.132:5000";
    private static final String JOIN = "join";
    private static final String OFFER = "offer";
    private static final String ACCEPT = "accept";
    private static final String DECLINE = "decline";
    private static final String CANDIDATE = "candidate";
    private static final String FINISH = "finish";
    private static final String LEAVE = "leave";
    private static final String TYPE = "type";
    RemoteListener remoteListener;
    Activity activity;
    VideoCapturerAndroid videoCapturerAndroid;
    AudioManager audioManager;
    PeerConnection peerConnection;
    PeerConnectionFactory peerConnectionFactory;
    VideoSource localVideoSource;
    VideoTrack localVideoTrack;
    AudioSource localAudioSource;
    AudioTrack localAudioTrack;
    MediaStream localMediaStream;
    MediaStream remoteMediaStream;
    VideoRenderer localVideoRenderer;
    VideoRenderer remoteVideoRenderer;
    SdpObserver sdpObserver;
    PeerConnection.Observer peerConnectionObserver;
    ArrayList<PeerConnection.IceServer> iceServers;

    int callType;

    private static final String VIDEO_TRACK_ID = "video";
    private static final String AUDIO_TRACK_ID = "audio";
    private static final String LOCAL_STREAM_ID = "stream";
    private static final String SDP_MID = "sdpMid";
    private static final String SDP_M_LINE_INDEX = "sdpMLineIndex";
    private static final String SDP = "sdp";

    static Socket socket;
    static CallManager callManager;
    ScreenManager screenManager;
    CallNotification callNotification;
    String myId;
    String myName;
    String hisId;
    JSONObject mLocalObject;
    JSONObject mRemoteObject;

    boolean choice;

    public static CallManager getInstance() {
        if (callManager == null)
            callManager = new CallManager();
        return callManager;
    }

    private CallManager() {
        screenManager = ScreenManager.getInstance();
    }

    public void setListener(CallNotification callNotification) {
        this.callNotification = callNotification;
    }

    public void setRemoteListener(RemoteListener remoteListener, Activity activity) {
        this.remoteListener = remoteListener;
        this.activity = activity;
    }

    public void createPeerConnection(Context context) {

        peerConnectionObserver = new PeerConnection.Observer() {
            @Override
            public void onSignalingChange(PeerConnection.SignalingState signalingState) {

            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
                if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
                    Log.i("tag", "Cuộc gọi kết thúc");
                }
            }

            @Override
            public void onIceConnectionReceivingChange(boolean b) {

            }

            @Override
            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                JSONObject object = new JSONObject();
                try {
                    object.put(SDP_MID, iceCandidate.sdpMid);
                    object.put(SDP_M_LINE_INDEX, iceCandidate.sdpMLineIndex);
                    object.put(SDP, iceCandidate.sdp);
                    object.put("hisId", hisId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("tag", "Connection send candidate ");
                socket.emit(CANDIDATE, object);
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                Log.i("tag", "onAddStream");
                remoteMediaStream = mediaStream;
            }

            @Override
            public void onRemoveStream(MediaStream mediaStream) {
                cancelConnection();
            }

            @Override
            public void onDataChannel(DataChannel dataChannel) {

            }

            @Override
            public void onRenegotiationNeeded() {

            }
        };
        iceServers = new ArrayList<>();
        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("turn:numb.viagenie.ca", "username", "password"));

        PeerConnectionFactory.initializeAndroidGlobals(context, true, true, true, null);
        peerConnectionFactory = new PeerConnectionFactory();
        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, new MediaConstraints(), peerConnectionObserver);
        Log.i("tag", "peerConnectionObserver " + peerConnection.toString());

    }

    public void joinSocket(String myId) {
        this.myId = myId;
        JSONObject myObject = new JSONObject();
        try {
            socket = IO.socket(SIGNALING_URI);
            myObject.put("myId", myId);
            socket.emit(JOIN, myObject);
            socket.on(OFFER, onOffer);
            socket.on(ACCEPT, onAccept);
            socket.on(DECLINE, onDecline);
            socket.on(CANDIDATE, onCandidate);
            socket.on(FINISH, onFinish);
            socket.on(TYPE, onType);
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void emitType(boolean type) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", type);
            object.put("hisId", hisId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(TYPE, object);
    }

    Emitter.Listener onType = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            boolean type = true;
            try {
                JSONObject object = (JSONObject) args[0];
                type = object.getBoolean("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final boolean finalType = type;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    remoteListener.setVisibleRemoteVideo(finalType);
                }
            });
        }
    };

    public void createOffer(final String myName, final String hisId, final int type) {
        peerConnection.addStream(localMediaStream);
        this.myName = myName;
        this.hisId = hisId;
        this.callType = type;
        sdpObserver = new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                mLocalObject = new JSONObject();
                Log.i("tag", "CallManager.createSdpObserver(): onCreateSuccess");
                peerConnection.setLocalDescription(sdpObserver, sessionDescription);
                try {
                    mLocalObject.put(SDP, sessionDescription.description);
                    mLocalObject.put("type", type);
                } catch (JSONException e) {
                }
                emitOffer();
            }

            @Override
            public void onSetSuccess() {
                Log.i("tag", "CallManager.createSdpObserver(): onSetSuccess");
            }

            @Override
            public void onCreateFailure(String s) {
                Log.i("tag", " CallManager.createSdpObserver(): Failure" + s);
            }

            @Override
            public void onSetFailure(String s) {
                Log.i("tag", "CallManager.createSdpObserver(): onSetFailure");
            }
        };
        peerConnection.createOffer(sdpObserver, new MediaConstraints());
    }

    public void emitOffer() {
        try {
            mLocalObject.put("myName", myName);
            mLocalObject.put("myId", myId);
            mLocalObject.put("hisId", hisId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(OFFER, mLocalObject);
    }

    Emitter.Listener onOffer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            peerConnection.addStream(localMediaStream);
            sdpObserver = new SdpObserver() {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    mLocalObject = new JSONObject();
                    Log.i("tag", "CallManager.createSdpObserver(): onCreateSuccess");
                    peerConnection.setLocalDescription(sdpObserver, sessionDescription);
                    Log.i("tag", "Success");
                    try {
                        mLocalObject.put(SDP, sessionDescription.description);
                    } catch (JSONException e) {
                    }
                    emitAnswer();
                }

                @Override
                public void onSetSuccess() {
                    Log.i("tag", "CallManager.createSdpObserver(): onSetSuccess");
                }

                @Override
                public void onCreateFailure(String s) {
                    Log.i("tag", " CallManager.createSdpObserver(): Failure" + s);
                }

                @Override
                public void onSetFailure(String s) {
                    Log.i("tag", "CallManager.createSdpObserver(): onSetFailure");
                }
            };
            try {
                mRemoteObject = (JSONObject) args[0];
                callType = mRemoteObject.getInt("type");
                SessionDescription sdp = new SessionDescription(SessionDescription.Type.OFFER, mRemoteObject.getString(SDP));
                Log.i("tag", "SessionDescription success");
                peerConnection.setRemoteDescription(sdpObserver, sdp);
                Log.i("tag", "setRemoteDescription success");
                callNotification.onNotifyCall(mRemoteObject.getString("myName"), mRemoteObject.getString("myId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    public void createAnswer(final boolean choice, final String hisId) {
        this.choice = choice;
        this.hisId = hisId;
        if (choice == accept)
            peerConnection.createAnswer(sdpObserver, new MediaConstraints());
        else emitAnswer();
    }

    public void emitAnswer() {
        try {
            if (mLocalObject == null)
                mLocalObject = new JSONObject();
            mLocalObject.put("myId", myId);
            mLocalObject.put("hisId", hisId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (choice == accept) {
            socket.emit(ACCEPT, mLocalObject);
            try {
                screenManager.openScreen(11, mRemoteObject.getString("myId"), null, callType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (choice == decline) {
            socket.emit(DECLINE, mLocalObject);
            screenManager.openScreen(8);
        }
    }

    Emitter.Listener onAccept = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                mRemoteObject = (JSONObject) args[0];
                Log.i("tag", "mRemoteObject " + mRemoteObject.toString());
                SessionDescription sdp = new SessionDescription(SessionDescription.Type.ANSWER, mRemoteObject.getString(SDP));
                Log.i("tag", "SessionDescription");
                peerConnection.setRemoteDescription(sdpObserver, sdp);
                Log.i("tag", "setRemoteDescription");
                screenManager.openScreen(11, mRemoteObject.getString("myId"), null, callType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("tag", "Connection receive createAnswer ");
        }
    };

    Emitter.Listener onDecline = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            screenManager.openScreen(8);
        }
    };

    Emitter.Listener onCandidate = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.i("tag", "Connection receive candidate ");
            try {
                JSONObject object = (JSONObject) args[0];
                Log.i("tag", "onCandidate ");
                peerConnection.addIceCandidate(new IceCandidate(object.getString(SDP_MID), object.getInt(SDP_M_LINE_INDEX), object.getString(SDP)));
                Log.i("tag", "addIceCandidate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void emitFinish(String hisId) {
        JSONObject object = new JSONObject();
        try {
            object.put("hisId", hisId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(FINISH, object);
        cancelConnection();
        screenManager.openScreen(8);
    }

    Emitter.Listener onFinish = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            cancelConnection();
            screenManager.openScreen(8);
        }
    };

    public void leaveSocket() {
        JSONObject object = new JSONObject();
        try {
            object.put("myId", myId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(LEAVE, object);
    }

    public interface CallNotification {
        void onNotifyCall(String hisName, String hisId);
    }

    public void setAudioManager(boolean speaker) {
        if (!speaker)
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        else
            audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(!speaker);
    }

    public void setVideoTrack(boolean camera) {
        localVideoTrack.setEnabled(camera);
    }

    public void setAudioTrack(boolean micro) {
        localAudioTrack.setEnabled(micro);
    }

    public void createMediaStream(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(true);

        videoCapturerAndroid = VideoCapturerAndroid.create(VideoCapturerAndroid.getNameOfFrontFacingDevice(), null);
        localVideoSource = peerConnectionFactory.createVideoSource(videoCapturerAndroid, new MediaConstraints());
        localVideoTrack = peerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, localVideoSource);
        localVideoTrack.setEnabled(true);

        localAudioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        localAudioTrack = peerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, localAudioSource);
        localAudioTrack.setEnabled(true);

        localMediaStream = peerConnectionFactory.createLocalMediaStream(LOCAL_STREAM_ID);
        localMediaStream.addTrack(localVideoTrack);
        localMediaStream.addTrack(localAudioTrack);
    }

    public void switchCamera() {
        videoCapturerAndroid.switchCamera(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void setVideoRenderer(GLSurfaceView glSurfaceView) {
        VideoRendererGui.setView(glSurfaceView, null);
        try {
            remoteVideoRenderer = VideoRendererGui.createGui(0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            localVideoRenderer = VideoRendererGui.createGui(70, 5, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            localVideoTrack.addRenderer(localVideoRenderer);
            remoteMediaStream.videoTracks.getFirst().addRenderer(remoteVideoRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void cancelConnection() {
        if (localMediaStream != null)
            peerConnection.removeStream(localMediaStream);
    }

    public interface RemoteListener {
        void setVisibleRemoteVideo(boolean type);
    }
}
